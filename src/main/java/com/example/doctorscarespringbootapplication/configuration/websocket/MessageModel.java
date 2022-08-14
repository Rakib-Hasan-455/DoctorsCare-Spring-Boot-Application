package com.example.doctorscarespringbootapplication.configuration.websocket;

public class MessageModel {

    private String senderName;
    private String receiverName;
    private String message;


    public MessageModel(String senderName, String receiverName, String message) {
        this.senderName = senderName;
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

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}

