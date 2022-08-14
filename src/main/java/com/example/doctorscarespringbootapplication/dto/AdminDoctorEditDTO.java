package com.example.doctorscarespringbootapplication.dto;

public class AdminDoctorEditDTO {
    private String id;
    private boolean enabled;
    private String name;
    private String page;
    private String DOB;
    private String email;
    private String phone;
    private String address;
    private String about;
    private String nid;
    private String doctortype;
    private String degrees;
    private String medicalcollege;
    private String appointmentfee;

    public AdminDoctorEditDTO(String id, boolean enabled, String name, String page, String DOB, String email, String phone, String address, String about, String nid, String doctortype, String degrees, String medicalcollege, String appointmentfee) {
        this.id = id;
        this.enabled = enabled;
        this.name = name;
        this.page = page;
        this.DOB = DOB;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.about = about;
        this.nid = nid;
        this.doctortype = doctortype;
        this.degrees = degrees;
        this.medicalcollege = medicalcollege;
        this.appointmentfee = appointmentfee;
    }

    public AdminDoctorEditDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getDoctortype() {
        return doctortype;
    }

    public void setDoctortype(String doctortype) {
        this.doctortype = doctortype;
    }

    public String getDegrees() {
        return degrees;
    }

    public void setDegrees(String degrees) {
        this.degrees = degrees;
    }

    public String getMedicalcollege() {
        return medicalcollege;
    }

    public void setMedicalcollege(String medicalcollege) {
        this.medicalcollege = medicalcollege;
    }

    public String getAppointmentfee() {
        return appointmentfee;
    }

    public void setAppointmentfee(String appointmentfee) {
        this.appointmentfee = appointmentfee;
    }
}
