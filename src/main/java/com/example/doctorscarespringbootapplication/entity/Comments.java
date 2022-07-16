package com.example.doctorscarespringbootapplication.entity;

import javax.persistence.*;

@Entity
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String commenterId;

    private String commenterName;

    private String commenterImage;

    private String comment;

    @ManyToOne
    private Posts posts;

    public Comments() {
    }

    public Comments(Long id, String commenterId, String commenterName, String commenterImage, Posts posts) {
        this.id = id;
        this.commenterId = commenterId;
        this.commenterName = commenterName;
        this.commenterImage = commenterImage;
        this.posts = posts;
    }

    public Comments(String commenterId, String commenterName, String commenterImage, String comment) {
        this.commenterId = commenterId;
        this.commenterName = commenterName;
        this.commenterImage = commenterImage;
        this.comment = comment;
    }

    public Comments(String commenterId, String commenterName, String commenterImage, String comment, Posts posts) {
        this.commenterId = commenterId;
        this.commenterName = commenterName;
        this.commenterImage = commenterImage;
        this.comment = comment;
        this.posts = posts;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public Posts getPosts() {
        return posts;
    }

    public void setPosts(Posts posts) {
        this.posts = posts;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Comments{" +
                "id=" + id +
                ", commenterId='" + commenterId + '\'' +
                ", commenterName='" + commenterName + '\'' +
                ", commenterImage='" + commenterImage + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
