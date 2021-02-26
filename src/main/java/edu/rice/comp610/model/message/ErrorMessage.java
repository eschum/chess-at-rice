package edu.rice.comp610.model.message;

public class ErrorMessage extends Message {
    String content;

    public ErrorMessage(String message) {
        type = "error";
        content = message;
    }
}
