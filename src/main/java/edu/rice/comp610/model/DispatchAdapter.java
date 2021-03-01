package edu.rice.comp610.model;

import edu.rice.comp610.model.game.Game;
import edu.rice.comp610.model.game.Player;
import org.eclipse.jetty.websocket.api.Session;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;


/**
 * This adapter interfaces with the view (paint objects) and the controller.
 */
public class DispatchAdapter {
    private static Session playerOne;
    private static Session playerTwo;
    private static PropertyChangeSupport pcs;
    public static int side = 600;
    private int gameCounter = 0;
    private Map<String, Game> allGames;
    Map<Session, Game> allSessions;

    /**
     * Constructor call.
     */
    public DispatchAdapter() {
        allGames = new HashMap<>();
        allSessions = new HashMap<>();
    }

    /**
     * Method: Add New Game.
     * Create a new game, and associate it to the directory of all games.
     * Instantiate a player with the username that wanted to great the new game.
     * @param username
     */
    public void addNewGame(String username) {
        Game game = new Game();
        //The gameCounter string will serve as the key for the game in the allGames map.
        allGames.put("Game" + gameCounter++, game);
        Player p1 = new Player(username);
        game.addPlayer(p1);

        System.out.print("New Game Started\n");
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
