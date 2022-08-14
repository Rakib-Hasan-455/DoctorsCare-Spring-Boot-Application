package com.example.doctorscarespringbootapplication.entity;

import javax.persistence.*;

@Entity
public class DoctorsSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(columnDefinition = "boolean default true")
    private boolean _10_00;
    @Column(columnDefinition = "boolean default true")
    private boolean _10_30;
    @Column(columnDefinition = "boolean default true")
    private boolean _11_00;
    @Column(columnDefinition = "boolean default true")
    private boolean _11_30;
    @Column(columnDefinition = "boolean default true")
    private boolean _12_00;
    @Column(columnDefinition = "boolean default true")
    private boolean _2_00;
    @Column(columnDefinition = "boolean default true")
    private boolean _2_30;
    @Column(columnDefinition = "boolean default true")
    private boolean _3_00;
    @Column(columnDefinition = "boolean default true")
    private boolean _3_30;

    @OneToOne
    private User user;

    public DoctorsSchedule() {

    }

    public DoctorsSchedule(int id, boolean _10_00, boolean _10_30, boolean _11_00, boolean _11_30, boolean _12_00, boolean _2_00, boolean _2_30, boolean _3_00, boolean _3_30, User user) {
        this.id = id;
        this._10_00 = _10_00;
        this._10_30 = _10_30;
        this._11_00 = _11_00;
        this._11_30 = _11_30;
        this._12_00 = _12_00;
        this._2_00 = _2_00;
        this._2_30 = _2_30;
        this._3_00 = _3_00;
        this._3_30 = _3_30;
        this.user = user;
    }

    public DoctorsSchedule(boolean _10_00, boolean _10_30, boolean _11_00, boolean _11_30, boolean _12_00, boolean _2_00, boolean _2_30, boolean _3_00, boolean _3_30, User user) {
        this._10_00 = _10_00;
        this._10_30 = _10_30;
        this._11_00 = _11_00;
        this._11_30 = _11_30;
        this._12_00 = _12_00;
        this._2_00 = _2_00;
        this._2_30 = _2_30;
        this._3_00 = _3_00;
        this._3_30 = _3_30;
        this.user = user;
    }

    public DoctorsSchedule(boolean _10_00, boolean _10_30, boolean _11_00, boolean _11_30, boolean _12_00, boolean _2_00, boolean _2_30, boolean _3_00, boolean _3_30) {
        this._10_00 = _10_00;
        this._10_30 = _10_30;
        this._11_00 = _11_00;
        this._11_30 = _11_30;
        this._12_00 = _12_00;
        this._2_00 = _2_00;
        this._2_30 = _2_30;
        this._3_00 = _3_00;
        this._3_30 = _3_30;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean is_10_00() {
        return _10_00;
    }

    public void set_10_00(boolean _10_00) {
        this._10_00 = _10_00;
    }

    public boolean is_10_30() {
        return _10_30;
    }

    public void set_10_30(boolean _10_30) {
        this._10_30 = _10_30;
    }

    public boolean is_11_00() {
        return _11_00;
    }

    public void set_11_00(boolean _11_00) {
        this._11_00 = _11_00;
    }

    public boolean is_11_30() {
        return _11_30;
    }

    public void set_11_30(boolean _11_30) {
        this._11_30 = _11_30;
    }

    public boolean is_12_00() {
        return _12_00;
    }

    public void set_12_00(boolean _12_00) {
        this._12_00 = _12_00;
    }

    public boolean is_2_00() {
        return _2_00;
    }

    public void set_2_00(boolean _2_00) {
        this._2_00 = _2_00;
    }

    public boolean is_2_30() {
        return _2_30;
    }

    public void set_2_30(boolean _2_30) {
        this._2_30 = _2_30;
    }

    public boolean is_3_00() {
        return _3_00;
    }

    public void set_3_00(boolean _3_00) {
        this._3_00 = _3_00;
    }

    public boolean is_3_30() {
        return _3_30;
    }

    public void set_3_30(boolean _3_30) {
        this._3_30 = _3_30;
    }

    @Override
    public String toString() {
        return "DoctorsSchedule{" +
                "id=" + id +
                ", _10_00=" + _10_00 +
                ", _10_30=" + _10_30 +
                ", _11_00=" + _11_00 +
                ", _11_30=" + _11_30 +
                ", _12_00=" + _12_00 +
                ", _2_00=" + _2_00 +
                ", _2_30=" + _2_30 +
                ", _3_00=" + _3_00 +
                ", _3_30=" + _3_30 +
//                ", user=" + user +
                '}';
    }
}
