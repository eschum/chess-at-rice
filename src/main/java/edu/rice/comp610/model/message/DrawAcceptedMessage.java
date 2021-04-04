package edu.rice.comp610.model.message;

import edu.rice.comp610.model.game.Player;

/**
 * Class ResignationMessage
 * This is a message that bears the news to the view
 * that a player has resigned.
 */
public class DrawAcceptedMessage extends Message {
    private final String name;
    private final String content;

    /**
     * Public Constructor
     * Construct a message that a given player has resigned.
     * @param p The player that has resigned.
     */
    public DrawAcceptedMessage(Player p) {
        type = "draw_accepted";
        this.name = p.getName();
        content = "Game Over. " + this.name + " has accepted the offer of a draw.";
    }
}
