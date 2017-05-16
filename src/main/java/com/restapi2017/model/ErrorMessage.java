package com.restapi2017.model;

public class ErrorMessage {

    Integer code;
    private String message;
    private String userMessage;
    String path;

    public ErrorMessage(Integer code, String message, String userMessage, String path) {
        this.code = code;
        this.message = message;
        this.userMessage = userMessage;
        this.path = path;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
