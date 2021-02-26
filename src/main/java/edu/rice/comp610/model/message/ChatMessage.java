package edu.rice.comp610.model.message;

import edu.rice.comp610.model.piece.Piece;

import java.util.ArrayList;

public class ChatMessage extends Message {
    String content;

    public ChatMessage(String message) {
        type = "chat";
        content = message;
    }
}
