package edu.rice.comp610.model.message;

import edu.rice.comp610.model.game.*;

public class PlayerJoin extends Message {
    private String name;

    public PlayerJoin(Player p) {
        type = "player_join";
        this.name = p.getName();
    }

    /**
     * Set Name: Helper function to set the name of the player.
     * @param n
     */
    public void setName(String n) {
        name = n;
    }

}
