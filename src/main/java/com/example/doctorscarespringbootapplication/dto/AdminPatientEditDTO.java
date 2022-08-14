package com.example.doctorscarespringbootapplication.dto;

public class AdminPatientEditDTO {
    private String id;
    private boolean enabled;
    private String name;
    private String page;
    private String DOB;
    private String email;
    private String phone;
    private String address;
    private String about;

    public AdminPatientEditDTO(String id, String page, String DOB, String email, String phone, String address, String about) {
        this.id = id;
        this.page = page;
        this.DOB = DOB;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.about = about;
    }

    public AdminPatientEditDTO() {
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
