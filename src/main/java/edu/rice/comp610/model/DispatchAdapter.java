package edu.rice.comp610.model;

import edu.rice.comp610.model.game.Game;
import edu.rice.comp610.model.game.Player;
import org.eclipse.jetty.websocket.api.Session;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


/**
 * This adapter interfaces with the view (paint objects) and the controller.
 */
public class DispatchAdapter {
    private static Session playerOne;
    private static Session playerTwo;
    private static PropertyChangeSupport pcs;
    public static int side = 600;

    /**
     * Constructor call.
     */
    public DispatchAdapter() {
        playerOne = null;
        playerTwo = null;
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
