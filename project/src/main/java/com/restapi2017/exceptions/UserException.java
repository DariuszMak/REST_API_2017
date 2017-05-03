package com.restapi2017.exceptions;

public class UserException extends RuntimeException {

    private final String message;
    private final String userMessage;

    public UserException(String message, String userMessage) {
        super(message);

        this.message = message;
        this.userMessage = userMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
