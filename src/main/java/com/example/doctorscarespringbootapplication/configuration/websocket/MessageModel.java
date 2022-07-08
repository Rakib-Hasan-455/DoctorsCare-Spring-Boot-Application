package com.example.doctorscarespringbootapplication.configuration.websocket;

public class MessageModel {
    private String receiverName;
    private String message;

    public MessageModel(String receiverName, String message) {
        this.receiverName = receiverName;
        this.message = message;
    }

    public MessageModel() {
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}

