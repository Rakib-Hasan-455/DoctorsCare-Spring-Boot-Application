package com.example.doctorscarespringbootapplication.dto;

public class UnsavePostDTO {
    private String postId;
    private String unsaverId;

    public UnsavePostDTO(String postId, String unsaverId) {
        this.postId = postId;
        this.unsaverId = unsaverId;
    }

    public UnsavePostDTO() {
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUnsaverId() {
        return unsaverId;
    }

    public void setUnsaverId(String unsaverId) {
        this.unsaverId = unsaverId;
    }
}
