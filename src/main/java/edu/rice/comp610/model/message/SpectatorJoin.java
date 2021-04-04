package edu.rice.comp610.model.message;

import edu.rice.comp610.model.game.Player;

/**
 * Class: Spectator Join
 * Send a message alerting that a spectator has joined.
 */
public class SpectatorJoin extends Message {
    private String name;

    /**
     * Public constructor.
     * @param p The Player that has joined the game as a spectator.
     */
    public SpectatorJoin(Player p) {
        type = "spectator_join";
        this.name = p.getName();
    }

    /**
     * Set Name: Helper function to set the name of the player.
     * @param n A String of the name of the spectator.
     */
    public void setName(String n) {
        name = n;
    }

}
