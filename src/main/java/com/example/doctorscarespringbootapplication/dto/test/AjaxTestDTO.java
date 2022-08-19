package com.example.doctorscarespringbootapplication.dto.test;

public class AjaxTestDTO {
    private String sentAt;
    private String message;

    public AjaxTestDTO(String sentAt, String message) {
        this.sentAt = sentAt;
        this.message = message;
    }

    public AjaxTestDTO() {
    }

    public String getSentAt() {
        return sentAt;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
