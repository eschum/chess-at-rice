package edu.rice.comp610.model.game;

import edu.rice.comp610.model.message.Message;
import edu.rice.comp610.model.message.PlayerJoin;

public class Player {
    private String name;
    private Message joinMessage;

    public Player(String userName) {
        name = userName;
        joinMessage = new PlayerJoin(this);
    }

    /**
     * Set Name: Helper function to set the name of the player.
     * @param n
     */
    public void setName(String n) {
        name = n;
    }

    public String getName() {
        return name;
    }

    public Message getJoinMessage() {
        return joinMessage;
    }
}
