package edu.rice.comp610.model.message;

/**
 * Message Superclass that implements the IMessage interface.
 * Subclasses will be different types of messages to send to
 * the view.
 */
public class Message implements IMessage {
    String type;

    /**
     * Public Constructor.
     * No specific action taken in the superclass.
     */
    Message() {}

    /**
     * Method: Set Type
     * Sets the type String with the appropriate qualifier
     * @param t String representing the type of the message.
     */
    public void setType(String t) {
        type = t;
    }
}
