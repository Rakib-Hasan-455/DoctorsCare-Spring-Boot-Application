package com.example.doctorscarespringbootapplication.entity;

import javax.persistence.*;

@Entity
public class DoctorsAdditionalInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String nid;

    private String doctortype;

    private String degrees;

    private String medicalcollege;

    private String appointmentfee;

    @OneToOne
    private User user;

    public DoctorsAdditionalInfo(int id, String nid, User user) {
        this.id = id;
        this.nid = nid;
        this.user = user;
    }

    public DoctorsAdditionalInfo(int id, String nid, String doctortype, String degrees, String medicalcollege, String appointmentfee, User user) {
        this.id = id;
        this.nid = nid;
        this.doctortype = doctortype;
        this.degrees = degrees;
        this.medicalcollege = medicalcollege;
        this.appointmentfee = appointmentfee;
        this.user = user;
    }

    public DoctorsAdditionalInfo(String nid, String doctortype, String degrees, String medicalcollege, String appointmentfee) {
        this.nid = nid;
        this.doctortype = doctortype;
        this.degrees = degrees;
        this.medicalcollege = medicalcollege;
        this.appointmentfee = appointmentfee;
    }

    public DoctorsAdditionalInfo() {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "DoctorsAdditionalInfo{" +
                "id=" + id +
                ", nid='" + nid + '\'' +
                ", doctortype='" + doctortype + '\'' +
                ", degrees='" + degrees + '\'' +
                ", medicalcollege='" + medicalcollege + '\'' +
                ", appointmentfee='" + appointmentfee + '\'' +
//                ", user=" + user +
                '}';
    }
}
