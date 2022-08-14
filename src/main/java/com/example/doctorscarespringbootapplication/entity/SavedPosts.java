package com.example.doctorscarespringbootapplication.entity;

import javax.persistence.*;

@Entity
public class SavedPosts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String saverId;
    @ManyToOne
    private Posts posts;

    public SavedPosts() {
    }

    public SavedPosts(int id, String saverId, Posts posts) {
        this.id = id;
        this.saverId = saverId;
        this.posts = posts;
    }

    public SavedPosts(String saverId, Posts posts) {
        this.saverId = saverId;
        this.posts = posts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSaverId() {
        return saverId;
    }

    public void setSaverId(String saverId) {
        this.saverId = saverId;
    }

    public Posts getPosts() {
        return posts;
    }

    public void setPosts(Posts posts) {
        this.posts = posts;
    }
}
