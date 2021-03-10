package edu.rice.comp610.model.message;

/**
 * IMessage interface defines what state and behavior the
 * message classes must have to be interpreted correctly
 * at the View
 */
public interface IMessage {
    /*
    There should be a String "type" that defines the type of the message
     */

    /**
     * Method: Set Type
     * Sets the type String with the appropriate qualifier
     * @param t String representing the type of the message.
     */
    void setType(String t);
}
