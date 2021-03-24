package edu.rice.comp610.model.message;

import edu.rice.comp610.model.game.Player;

/**
 * Class DrawRequestMessage
 * Send a message to the other player to ask for a draw.
 * The receiving player will either accept or deny.
 */
public class DrawRequestMessage extends Message {
    private String content;

    /**
     * Public Constructor
     * Construct a message that a given player has resigned.
     */
    public DrawRequestMessage(Player p) {
        type = "draw_request_from_server";
        content = p.getName() + " has requested a draw. Do you accept?";
    }
}
