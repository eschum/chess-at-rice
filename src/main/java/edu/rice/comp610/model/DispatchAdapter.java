package edu.rice.comp610.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.rice.comp610.model.game.Game;
import edu.rice.comp610.model.game.Player;
import org.eclipse.jetty.websocket.api.Session;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * This adapter interfaces with the view (paint objects) and the controller.
 */
public class DispatchAdapter {
    private static PropertyChangeSupport pcs;
    public static int side = 600;
    private int gameCounter = 0;
    private ArrayList<Game> allGames;
    private Map<String, Game> allPlayersToGames;
    private Map<Session, Game> allSessions;
    private Map<String, Player> allPlayers;  //Need to have a hashmap to quickly set the session of each player
    public static Gson gson;

    /**
     * Constructor call.
     */
    public DispatchAdapter() {
        gson = new Gson();
        allPlayersToGames = new HashMap<>();
        allSessions = new HashMap<>();
        allPlayers = new HashMap<>();
        allGames = new ArrayList<>();
    }

    /**
     * Method: Get All Games
     * Accessor method to return an Array List of all the games.
     *
     * @return An array list of all game objects.
     */
    public ArrayList<JsonObject> getAllGames() {
        ArrayList<JsonObject> gameData = new ArrayList<>();
        for (Game game: allGames) {
            JsonObject obj = new JsonObject();
            obj.addProperty("gameID", game.getID());
            Player lightPlayer = game.getLightPlayer();
            Player darkPlayer = game.getDarkPlayer();
            if (lightPlayer != null)
                obj.addProperty("lightPlayer", lightPlayer.getName());
            else
                obj.addProperty("lightPlayer", "NULL");
            if (darkPlayer != null)
                obj.addProperty("darkPlayer", darkPlayer.getName());
            else
                obj.addProperty("darkPlayer", "NULL");

            gameData.add(obj);
        }
        return gameData;
    }



    /**
     * Handle messages from the WebSocketController
     * @param userSession
     * @param message
     */
    public void processMessage(Session userSession, String message) {
        //System.out.print(message);
        JsonObject parsedMsg = gson.fromJson(message, JsonObject.class);

        String type = parsedMsg.get("type").toString();
        type = type.substring(1, type.length() - 1);

        System.out.print(type);
        //Take action based on message type.
        if (type.equals("join")) {
            //Get the username and role fields.
            String username = parsedMsg.get("username").toString();
            username = username.substring(1, username.length() - 1);
            String role = parsedMsg.get("role").toString();
            role = role.substring(1, role.length() - 1);
            System.out.print(username);
            System.out.print(role);
            connectUser(userSession, username, role);

        } else if (type.equals("move")) {
            //Get the to and from fields.
            String from = parsedMsg.get("fromLoc").toString();
            from = from.substring(1, from.length() - 1);
            String to = parsedMsg.get("toLoc").toString();
            to = to.substring(1, to.length() - 1);
            //Delegate that game to process the action and follow up with response.
            allSessions.get(userSession).processMove(userSession, from, to);

        } else if (type.equals("chat")) {
            System.out.print("Processed a chat message");
            //Get the content field.
            String content = parsedMsg.get("content").toString();
            content = content.substring(1, content.length() - 1);
            //Delegate that game to process sending the chat to all participants.
            allSessions.get(userSession).processChat(userSession, content);
        }
    }

    private void connectUser(Session userSession, String username, String role) {
        Player player = allPlayers.get(username);

        if (role.equals("lightPlayer")) {
            //Set the Session of the player.
            player.setSession(userSession);
            allSessions.put(userSession, allPlayersToGames.get(username));
            //Message player 1.
            try {
                player.getSession().getRemote().sendString(gson.toJson(player.getJoinMessage()));
            } catch (IOException e) {
                System.out.println("IO Exception");
            }
        }
    }

    /**
     * Method: Add New Game.
     * Create a new game, and associate it to the directory of all games.
     * Instantiate a player with the username that wanted to great the new game.
     * @param username
     */
    public String addNewGame(String username) {
        //The gameCounter string will serve as the key for the game in the allGames map.
        String gameID = "Game" + gameCounter++;
        Game game = new Game(gameID);
        //allGames.put(gameID, game);
        Player p1 = new Player(username);
        game.addPlayer(p1);
        allPlayers.put(username, p1);
        allPlayersToGames.put(username, game);
        allGames.add(game);

        System.out.print("New Game Started\n");
        return gameID;
    }



    /**
     * Connect Players: Method to associate our connection
     * @return
     */
    public static int connectPlayer(Player player, Game game) {
        if (game.getLightPlayer() == null) {
            game.addPlayer(player);
            return 1;
        } else if (game.getDarkPlayer() == null) {
            game.addPlayer(player);
            return 2;
        } else {
            //return 0 if spectator.

            //Add here to add the spectator
            return 0;
        }
    }

    /**
     * Accessor method to return the static pcs - so objects can call the collision changes themselves.
     * @return the PropertyChangeSupport object.
     */
    public static PropertyChangeSupport getPCS() {
        return pcs;
    }


    /**
     * Call the update method on all the ball observers to update their position in the ball world.
     */
    public PropertyChangeListener[] updateBallWorld() {
        pcs.firePropertyChange("theClock", false, true);
        return pcs.getPropertyChangeListeners("theClock");
    }

    /**
     * Generate a random number.
     * Made public for easy access from Strategy classes.
     * @param base  The minimum value
     * @param limit The maximum number from the base
     * @return A randomly generated number
     */
    public static int getRnd(int base, int limit) {
        return (int)Math.floor(Math.random() * limit + base);
    }

    /**
     * Remove Single Listener
     * Remove a single listener from all the event listener registries, and from the collision
     * @param pcl - PropertyChangeListener
     */
    public void removeSingleListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener("theClock", pcl);
        pcs.removePropertyChangeListener("collision-detect", pcl);
    }
}
