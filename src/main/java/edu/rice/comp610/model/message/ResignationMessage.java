package edu.rice.comp610.model.message;

import edu.rice.comp610.model.game.Player;

/**
 * Class ResignationMessage
 * This is a message that bears the news to the view
 * that a player has resigned.
 */
public class ResignationMessage extends Message {
    private final String name;
    private final String content;

    /**
     * Public Constructor
     * Construct a message that a given player has resigned.
     * @param p The player that has resigned.
     */
    public ResignationMessage(Player p) {
        type = "resignation";
        this.name = p.getName();
        content = "Game Over. " + this.name + " has resigned.";
    }
}
