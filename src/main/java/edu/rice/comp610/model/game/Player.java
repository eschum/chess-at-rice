package edu.rice.comp610.model.game;

import edu.rice.comp610.model.message.Message;
import edu.rice.comp610.model.message.PlayerJoin;
import org.eclipse.jetty.websocket.api.Session;

public class Player {
    private String name;
    private Message joinMessage;
    private Session sess;
    public boolean isLightPlayer;
    public boolean isDarkPlayer;
    public boolean isSpectator;

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

    /**
     * setSession: Helper function to save the session of the player
     * @param userSession
     */
    public void setSession(Session userSession) {
        sess = userSession;
    }

    /**
     * Get Session: return the Session associated with this player.
     * @return
     */
    public Session getSession() {
        return sess;
    }

}
