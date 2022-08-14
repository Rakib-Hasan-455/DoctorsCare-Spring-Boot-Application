package com.example.doctorscarespringbootapplication.dto;

import com.example.doctorscarespringbootapplication.entity.Posts;

public class LikesDTO {
    private String postId;
    private String likerId;

    public LikesDTO(String postId, String likerId) {
        this.postId = postId;
        this.likerId = likerId;
    }

    public LikesDTO() {
    }

    public String getLikerId() {
        return likerId;
    }

    public void setLikerId(String likerId) {
        this.likerId = likerId;
    }


    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
