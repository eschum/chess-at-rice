package edu.rice.comp610.model.message;

import edu.rice.comp610.model.game.*;

/**
 * Class PlayerJoin
 * Message class to send to a player when newly joining the game.
 */
public class PlayerJoin extends Message {
    private String name;

    /**
     * Public Constructor
     * Set the message
     * @param p A Player object.
     */
    public PlayerJoin(Player p) {
        setType("player_join");
        this.name = p.getName();
    }

    /**
     * Set Name: Helper function to set the name of the player.
     * @param n A string of the name of the player.
     */
    public void setName(String n) {
        name = n;
    }

    /**
     * Method: Get Name
     * Accessor method to return the name of the player that is joining.
     * @return String of a name.
     */
    public String getName() { return this.name; }

}
