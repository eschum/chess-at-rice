package edu.rice.comp610.model.message;

import edu.rice.comp610.model.game.Player;

/**
 * Class Player Leave
 * This is a message that bears the news to the view
 * that one of the players has left.
 * The message type is encoded in String 'type' and
 * the value of the alert is encoded in String content.
 */
public class PlayerLeave extends Message {
    private String name;
    private String content;

    /**
     * Public Constructor
     * @param p The Player that has left.
     */
    public PlayerLeave(Player p) {
        type = "player_leave";
        this.name = p.getName();
        content = this.name + " has forfeited, the game is over";
    }
}
