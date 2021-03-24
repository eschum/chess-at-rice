package edu.rice.comp610.model.message;

import edu.rice.comp610.model.game.Player;

/**
 * Class Draw Denied Message
 * This is a message that bears the news to the view
 * that a player has resigned.
 */
public class DrawDeniedMessage extends Message {
    private String name;
    private String content;

    /**
     * Public Constructor
     * Construct a message that a given player has resigned.
     * @param p The player that has resigned.
     */
    public DrawDeniedMessage(Player p) {
        type = "draw_denied";
        this.name = p.getName();
        content = this.name + " did not accept your draw. Continue play!";
    }
}
