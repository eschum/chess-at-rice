package edu.rice.comp610.model.message;

/**
 * Class Error Message
 * Send a message with an error message.
 */
public class ErrorMessage extends Message {
    String content;

    /**
     * Public constructor
     * @param message A String with error message to deliver to the view.
     */
    public ErrorMessage(String message) {
        type = "error";
        content = message;
    }
}
