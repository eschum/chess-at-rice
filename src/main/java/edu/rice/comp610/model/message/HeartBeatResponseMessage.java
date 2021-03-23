package edu.rice.comp610.model.message;

/**
 * Class HeartBeatResponseMessage
 * Send a message responding to a heart beat.
 */
public class HeartBeatResponseMessage extends Message {
    private static HeartBeatResponseMessage ref;

    /**
     * Private constructor
     * Implement the singleton design pattern, because all the heartbeat
     * responses will be the same.
     */
    private HeartBeatResponseMessage() {
        type = "heartbeat_response";
    }

    /**
     * Method: Get Instance
     * @return The singleton HeartBeatResponseMessage
     */
    public static HeartBeatResponseMessage getInstance() {
        if (ref == null) ref = new HeartBeatResponseMessage();
        return ref;
    }
}
