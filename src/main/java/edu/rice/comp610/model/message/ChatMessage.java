package edu.rice.comp610.model.message;

/**
 * Class Chat Message
 * Send a message with the contents of a chat message from the user.
 */
public class ChatMessage extends Message {
    String content;

    /**
     * Public Constructor
     * @param message A String with the value of the chat.
     */
    public ChatMessage(String message) {
        type = "chat";
        content = message;
    }
}
