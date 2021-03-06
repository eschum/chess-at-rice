package edu.rice.comp610.model.message;

import edu.rice.comp610.model.game.Player;

/**
 * Class Spectator Leave
 * This is a message that bears the news to the view
 * that a spectator has left.
 * The message type is encoded in String 'type' and
 * the value of the alert is encoded in String content.
 */
public class SpectatorLeave extends Message {
    private final String name;
    private final String content;

    /**
     * Public Constructor
     * @param p The Player (spectator) that is leaving.
     */
    public SpectatorLeave(Player p) {
        type = "spectator_leave";
        this.name = p.getName();
        content = "Spectator " + this.name + " has disconnected";
    }

}
