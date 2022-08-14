package com.example.doctorscarespringbootapplication.entity;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Entity
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private Date postDate;
    private Time postTime;
    @Column(length = 10000)
    private String coverPhoto;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "posts")
    private List<Likes> likesList;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "posts")
    private List<Comments> commentsList;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "posts")
    private List<SavedPosts> savedPostsList;
    @ManyToOne
    private User user;
    @Column(columnDefinition = "TEXT")
    private String postContent;

    public Posts(int id, Date postDate, Time postTime, String coverPhoto, List<Likes> likesList, String postContent) {
        this.id = id;
        this.postDate = postDate;
        this.postTime = postTime;
        this.coverPhoto = coverPhoto;
        this.likesList = likesList;
        this.postContent = postContent;
    }

    public Posts(Date postDate, Time postTime, String coverPhoto, String postContent) {
        this.postDate = postDate;
        this.postTime = postTime;
        this.coverPhoto = coverPhoto;
        this.postContent = postContent;
    }

    public Posts() {
    }


    public List<SavedPosts> getSavedPostsList() {
        return savedPostsList;
    }

    public void setSavedPostsList(List<SavedPosts> savedPostsList) {
        this.savedPostsList = savedPostsList;
    }

    public List<Comments> getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(List<Comments> commentsList) {
        this.commentsList = commentsList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public Time getPostTime() {
        return postTime;
    }

    public void setPostTime(Time postTime) {
        this.postTime = postTime;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Likes> getLikesList() {
        return likesList;
    }

    public void setLikesList(List<Likes> likesList) {
        this.likesList = likesList;
    }

    @Override
    public String toString() {
        return "Posts{" +
                "id=" + id +
                ", postDate=" + postDate +
                ", postTime=" + postTime +
                ", coverPhoto='" + coverPhoto + '\'' +
                ", postContent='" + postContent + '\'' +
                '}';
    }
}
