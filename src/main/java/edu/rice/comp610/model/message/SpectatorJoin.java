package edu.rice.comp610.model.message;

import edu.rice.comp610.model.game.Player;

public class SpectatorJoin extends Message {
    private String name;

    public SpectatorJoin(Player p) {
        type = "spectator_join";
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
