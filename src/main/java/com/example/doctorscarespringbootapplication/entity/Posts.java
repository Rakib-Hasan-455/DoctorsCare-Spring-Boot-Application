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

    private String doctorID;

    private String doctorName;

    private Date postDate;

    private Time postTime;

    private String coverPhoto;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "posts")
    private List<Likes> likesList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "posts")
    private List<Comments> commentsList;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "posts")
    private SavedPosts savedPostsList;

    @Column(length = 5000)
    private String postContent;

    public Posts(int id, String doctorID, String doctorName, Date postDate, Time postTime, String coverPhoto, List<Likes> likesList, String postContent) {
        this.id = id;
        this.doctorID = doctorID;
        this.doctorName = doctorName;
        this.postDate = postDate;
        this.postTime = postTime;
        this.coverPhoto = coverPhoto;
        this.likesList = likesList;
        this.postContent = postContent;
    }

    public Posts(String doctorID, String doctorName, Date postDate, Time postTime, String coverPhoto, String postContent) {
        this.doctorID = doctorID;
        this.doctorName = doctorName;
        this.postDate = postDate;
        this.postTime = postTime;
        this.coverPhoto = coverPhoto;
        this.postContent = postContent;
    }

    public Posts() {
    }


    public SavedPosts getSavedPostsList() {
        return savedPostsList;
    }

    public void setSavedPostsList(SavedPosts savedPostsList) {
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

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
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

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
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
                ", doctorID='" + doctorID + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", postDate=" + postDate +
                ", postTime=" + postTime +
                ", coverPhoto='" + coverPhoto + '\'' +
                ", postContent='" + postContent + '\'' +
                '}';
    }
}
