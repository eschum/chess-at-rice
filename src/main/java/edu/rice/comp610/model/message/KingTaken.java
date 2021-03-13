package edu.rice.comp610.model.message;

import edu.rice.comp610.model.game.Player;

/**
 * Class Player Leave
 * This is a message that bears the news to the view
 * that one of the players has left.
 * The message type is encoded in String 'type' and
 * the value of the alert is encoded in String content.
 */
public class KingTaken extends Message {
    private String name;
    private String content;

    /**
     * Public Constructor
     * Construct a message that the king was taken.
     * @param p The player that has won by taking the opponent's King.
     */
    public KingTaken(Player p) {
        type = "king_taken";
        this.name = p.getName();
        content = "Game Over. " + this.name + " is victorious by taking the opponent's King.";
    }
}
