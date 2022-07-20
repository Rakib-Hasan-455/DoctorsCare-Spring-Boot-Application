package com.example.doctorscarespringbootapplication.dto;

public class DeleteUserDTO {
    private String userId;

    public DeleteUserDTO(String userId) {
        this.userId = userId;
    }

    public DeleteUserDTO() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

