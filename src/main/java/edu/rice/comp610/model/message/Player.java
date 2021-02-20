package edu.rice.comp610.model.message;

public class Player extends Message{
    private String name;

    public Player() {
        type = "player";
    }

    /**
     * Set Name: Helper function to set the name of the player.
     * @param n
     */
    public void setName(String n) {
        name = n;
    }
}
