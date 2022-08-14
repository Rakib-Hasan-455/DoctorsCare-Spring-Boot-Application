package com.example.doctorscarespringbootapplication.dto;

public class SavedPostsDTO {
    private String saverId;
    private String postId;

    public SavedPostsDTO(String saverId, String postId) {
        this.saverId = saverId;
        this.postId = postId;
    }
    public SavedPostsDTO() {
    }

    public String getSaverId() {
        return saverId;
    }

    public void setSaverId(String saverId) {
        this.saverId = saverId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
