package com.example.doctorscarespringbootapplication.dto;

public class CommentsDTO {
    private String postId;
    private String commenterId;
    private String commenterName;
    private String commenterImage;
    private String comment;

    public CommentsDTO() {
    }

    public CommentsDTO(String postId, String commenterId, String commenterName, String commenterImage, String comment) {
        this.postId = postId;
        this.commenterId = commenterId;
        this.commenterName = commenterName;
        this.commenterImage = commenterImage;
        this.comment = comment;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getCommenterId() {
        return commenterId;
    }

    public void setCommenterId(String commenterId) {
        this.commenterId = commenterId;
    }

    public String getCommenterName() {
        return commenterName;
    }

    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }

    public String getCommenterImage() {
        return commenterImage;
    }

    public void setCommenterImage(String commenterImage) {
        this.commenterImage = commenterImage;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
