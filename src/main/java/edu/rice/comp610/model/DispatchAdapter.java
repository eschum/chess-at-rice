package edu.rice.comp610.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.rice.comp610.model.game.Game;
import edu.rice.comp610.model.game.Player;
import edu.rice.comp610.model.message.StartGame;
import org.eclipse.jetty.websocket.api.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * This adapter interfaces with the view (paint objects) and the controller.
 */
public class DispatchAdapter {
    public static int side = 600;
    private int gameCounter = 0;
    private final ArrayList<Game> allGames;
    private final Map<String, Game> allPlayersToGames;
    private final Map<Session, Game> allSessions;
    private final Map<String, Player> allPlayers;  //Need to have a hashmap to quickly set the session of each player
    public static Gson gson;
    private HashMap<String, String> credentials;

    /**
     * Constructor call.
     */
    public DispatchAdapter() {
        gson = new Gson();
        allPlayersToGames = new HashMap<>();
        allSessions = new HashMap<>();
        allPlayers = new HashMap<>();
        allGames = new ArrayList<>();
        credentials = new HashMap<>();
        initCredentials();
    }

    public JsonObject validateCredentials(String username, String password) {
        JsonObject response = new JsonObject();

        //Return response based on whether or not the ID / password is correct
        if (credentials.containsKey(username) &&
                credentials.get(username).equals(password)) {
            response.addProperty("auth", "true");

        } else {
            response.addProperty("auth", "false");
        }
        System.out.print(true);


        return response;
    }

    /**
     * Function: Init Credentials
     * Hard-code a set number of access credentials to test
     * authentication. In the future, could be expanded to a database.
     */
    private void initCredentials() {
        credentials.put("eric", "ricestudent");
        credentials.put("zoran", "ricefaculty");
        credentials.put("testaccount", "test-account-pw");
    }

    /**
     * Method: Get All Games
     * Accessor method to return an Array List of all the games.
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
     * @param userSession - session of the current connection from which a message was received
     * @param message - String of the message contents.
     */
    public void processMessage(Session userSession, String message) {
        //System.out.print(message);
        JsonObject parsedMsg = gson.fromJson(message, JsonObject.class);

        String type = parsedMsg.get("type").toString();
        type = type.substring(1, type.length() - 1);

        System.out.print(type);
        //Take action based on message type.
        //Get the username and role fields.
        //Get the to and from fields.
        //Delegate that game to process the action and follow up with response.
        //Get the content field.
        //Delegate that game to process sending the chat to all participants.
        switch (type) {
            case "join" -> {
                String username = parsedMsg.get("username").toString();
                username = username.substring(1, username.length() - 1);
                String role = parsedMsg.get("role").toString();
                role = role.substring(1, role.length() - 1);
                connectUser(userSession, username, role);
            }
            case "move" -> {
                String from = parsedMsg.get("fromLoc").toString();
                from = from.substring(1, from.length() - 1);
                String to = parsedMsg.get("toLoc").toString();
                to = to.substring(1, to.length() - 1);
                allSessions.get(userSession).processMove(userSession, from, to);
            }
            case "chat" -> {
                System.out.print("Processed a chat message");
                String content = parsedMsg.get("content").toString();
                content = content.substring(1, content.length() - 1);
                allSessions.get(userSession).processChat(userSession, content);
            }
        }
    }

    /**
     * Method: Connect User.
     * Takes action when the join message is received from the view/client
     * @param userSession - session of the current connection from which a message was received
     * @param username - user name string of the user in this connection.
     * @param role - string of the role (lightPlayer, darkPlayer, or spectator)
     */
    private void connectUser(Session userSession, String username, String role) {
        Player player = allPlayers.get(username);
        Game game = allPlayersToGames.get(username);

        //Invariant code - do it no matter what the role of the player.
        player.setSession(userSession);
        allSessions.put(userSession, game);

        game.addEntity(player);

        switch (role) {
            case "lightPlayer":

                //Message player 1.
                try {
                    player.getSession().getRemote().sendString(gson.toJson(player.getJoinMessage()));
                } catch (IOException e) {
                    System.out.println("IO Exception");
                }

                break;
            case "darkPlayer":
                Player lightPlayer = game.getLightPlayer();
                Player darkPlayer = game.getDarkPlayer();

                try {
                    //Message dark player that light player is connected
                    darkPlayer.getSession().getRemote()
                            .sendString(gson.toJson(lightPlayer.getJoinMessage()));
                    //Message dark player and light player that dark player is connected.
                    darkPlayer.getSession().getRemote()
                            .sendString(gson.toJson(darkPlayer.getJoinMessage()));
                    lightPlayer.getSession().getRemote()
                            .sendString(gson.toJson(darkPlayer.getJoinMessage()));

                    //Send start game message to both players, with proper permissions.
                    lightPlayer.getSession().getRemote()
                            .sendString(gson.toJson(sendStartMsg(game, true, false, false)));
                    darkPlayer.getSession().getRemote()
                            .sendString(gson.toJson(sendStartMsg(game, false, true, false)));
                } catch (IOException e) {
                    System.out.println("IO Exception");
                }

                break;
            case "spectator":
                game.connectSpectator(player);
                break;
        }
    }

    /**
     * Send Start Message: Helper function to send a message to start the game.
     * Just send piece locations and current status.
     * @param game - current game object context.
     * @param lightPlayer - boolean as to whether or not this player is lightPlayer
     * @param darkPlayer - boolean as to whether or not this player is darkPlayer
     * @param spectator - boolean as to whether or not this player is spectator
     * @return A StartGame message with the appropriate fields configured.
     */
    private StartGame sendStartMsg(Game game, boolean lightPlayer, boolean darkPlayer,
                                   boolean spectator) {
        return new StartGame(game.getLightPieces(), game.getDarkPieces(),
                lightPlayer, darkPlayer, spectator);
    }

    /**
     * Method: Add New Game.
     * Create a new game, and associate it to the directory of all games.
     * Instantiate a player with the username that wanted to great the new game.
     * @param username - username for the player that will be added to the new game.
     * @return - String of the game ID for the new game that was started.
     */
    public String addNewGame(String username) {
        //The gameCounter string will serve as the key for the game in the allGames map.
        String gameID = "Game" + gameCounter++;
        Game game = new Game(gameID);

        Player p1 = new Player(username);
        game.addPlayer(p1);
        allPlayers.put(username, p1);
        allPlayersToGames.put(username, game);
        allGames.add(game);

        System.out.print("New Game Started\n");
        return gameID;
    }

    /**
     * Method: Join Game.
     * This method instantiates a player with a given username and joins
     * to a specified game.
     * Will return false if unsuccessful - may be the case that the game was already deleted, etc.
     * @param username - username of the new player for joining the game.
     * @param gameID - String of the game to join.
     * @return - A string indicating the role the user joined as. "null", "darkPlayer", or "spectator"
     */
    public String joinGame(String username, String gameID) {
        //Find the correct game. Exit early if there is no game.
        Game game = null;
        for (Game g: allGames) {
            if (g.getID().equals(gameID)) {
                //If we have a match, associate the Game reference and exit early.
                game = g;
                break;
            }
        }
        //Exit with false result if the game no longer exists.
        if (game == null) return "null";

        //Game exists - instantiate the player.
        Player p2 = new Player(username);
        allPlayers.put(username, p2);
        allPlayersToGames.put(username, game);
        String status;

        //Join as dark player by default.
        if (game.getDarkPlayer() == null) {
            game.addPlayer(p2);  //session will be blank until the player "dials in" and sends the
            //connection message.
            System.out.print(p2.getName() + " joined as dark player\n");
            status = "darkPlayer";

        } else {
            //Otherwise, join as spectator.
            game.addSpectator(p2);
            System.out.print(p2.getName() + " joined as a spectator\n");
            status = "spectator";
        }

        return status;
    }
}
