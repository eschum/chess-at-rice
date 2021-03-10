package edu.rice.comp610.model.game;

import edu.rice.comp610.model.message.Message;
import edu.rice.comp610.model.message.PlayerJoin;
import edu.rice.comp610.model.message.SpectatorJoin;
import org.eclipse.jetty.websocket.api.Session;

/**
 * Class Player
 * State and behavior for each player that is interacting with the
 * Chess@Rice platform.
 */
public class Player {
    private String name;
    private final Message joinMessage;
    private Session sess;

    /**
     * Public constructor to instantiate a player.
     * This version is when we already have a session.
     *
     * This is mostly used in testing only.
     * @param userName A string of the user identifier.
     * @param userSession The session of the connecting user.
     */
    public Player(String userName, Session userSession) {
        name = userName;
        sess = userSession;
        joinMessage = new PlayerJoin(this);
    }

    /**
     * Public constructor to instantiate a player.
     * This version is when we do not yet have an associated session.
     * This is the version that will be called in production platform access.
     * @param userName A string of the user identifier.
     */
    public Player(String userName) {
        name = userName;
        joinMessage = new PlayerJoin(this);
    }

    /**
     * Set Name: Helper function to set the name of the player.
     * @param n The String name of the player.
     */
    public void setName(String n) {
        name = n;
    }

    /**
     * Helper mutator method to set the session.
     * @param userSession The session of the player that is accessing the platform.
     */
    public void setSession(Session userSession) {
        sess = userSession;
    }

    /**
     * Method: Get Name
     * Accessor method to return the name of the player.
     * @return String of the player name.
     */
    public String getName() {
        return name;
    }

    /**
     * Method: Get Join Message
     * Return the join message for this player.
     * @return A JoinMessage that was created on player instantiation.
     */
    public Message getJoinMessage() {
        return joinMessage;
    }

    /**
     * Method: Get SpectatorJoin
     * Call this method to instantiate a SpectatorJoin message with the
     * state of this instance of the Player
     * @return SpectatorJoin message related to this player.
     */
    public Message getSpectatorJoin() {
        return new SpectatorJoin(this);
    }

    /**
     * Get Session: return the Session associated with this player.
     * @return The Session that is associated with the player .
     */
    public Session getSession() {
        return sess;
    }
}
