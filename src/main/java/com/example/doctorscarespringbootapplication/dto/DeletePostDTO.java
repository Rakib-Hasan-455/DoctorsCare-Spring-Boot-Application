package com.example.doctorscarespringbootapplication.dto;

public class DeletePostDTO {
    private String postId;

    public DeletePostDTO() {
    }

    public DeletePostDTO(String postId) {
        this.postId = postId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
