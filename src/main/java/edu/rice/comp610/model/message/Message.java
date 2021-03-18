package edu.rice.comp610.model.message;

/**
 * Message Superclass.
 * Subclasses will be different types of messages to send to
 * the view.
 * Class is abstract as it would not make sense / be possible to declare a message on its own.
 */
public abstract class Message {
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
