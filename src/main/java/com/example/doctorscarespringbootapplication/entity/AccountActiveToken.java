package com.example.doctorscarespringbootapplication.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class AccountActiveToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(length = 1000, unique = true)
    private String token;

    private Date tokenGenerationDate;

    @OneToOne
    private User user;

    private boolean emailIsVerified;

    public AccountActiveToken(long id, String token, Date tokenGenerationDate, User user, boolean emailIsVerified) {
        this.id = id;
        this.token = token;
        this.tokenGenerationDate = tokenGenerationDate;
        this.user = user;
        this.emailIsVerified = emailIsVerified;
    }

    public AccountActiveToken() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getTokenGenerationDate() {
        return tokenGenerationDate;
    }

    public void setTokenGenerationDate(Date tokenGenerationDate) {
        this.tokenGenerationDate = tokenGenerationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isEmailIsVerified() {
        return emailIsVerified;
    }

    public void setEmailIsVerified(boolean emailIsVerified) {
        this.emailIsVerified = emailIsVerified;
    }
}
