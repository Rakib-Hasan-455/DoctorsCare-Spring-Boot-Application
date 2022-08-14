package com.example.doctorscarespringbootapplication.entity;

import javax.persistence.*;

@Entity
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    private Posts posts;
    private String likerId;

    public Likes(int id, Posts post, String likerId) {
        this.id = id;
        this.posts = post;
        this.likerId = likerId;
    }

    public Likes(Posts post, String likerId) {
        this.posts = post;
        this.likerId = likerId;
    }

    public Likes() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Posts getPost() {
        return posts;
    }

    public void setPost(Posts post) {
        this.posts = post;
    }

    public String getLikerId() {
        return likerId;
    }

    public void setLikerId(String likerId) {
        this.likerId = likerId;
    }
}
