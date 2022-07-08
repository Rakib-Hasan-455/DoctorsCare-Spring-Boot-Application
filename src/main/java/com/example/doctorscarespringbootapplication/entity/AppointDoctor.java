package com.example.doctorscarespringbootapplication.entity;

import javax.persistence.*;
import java.sql.Time;

@Entity
public class AppointDoctor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String patientID;

    private String doctorID;

    private String doctorFee;

    @Basic
    private Time appointmentTime;

    private String appointmentDate;

    public AppointDoctor(int id, String patientID, String doctorID, String doctorFee, Time appointmentTime, String appointmentDate) {
        this.id = id;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.doctorFee = doctorFee;
        this.appointmentTime = appointmentTime;
        this.appointmentDate = appointmentDate;
    }

    public AppointDoctor(String patientID, String doctorID, String doctorFee, Time appointmentTime, String appointmentDate) {
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.doctorFee = doctorFee;
        this.appointmentTime = appointmentTime;
        this.appointmentDate = appointmentDate;
    }

    public AppointDoctor() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getDoctorFee() {
        return doctorFee;
    }

    public void setDoctorFee(String doctorFee) {
        this.doctorFee = doctorFee;
    }

    public Time getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Time appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    @Override
    public String toString() {
        return "AppointDoctor{" +
                "id=" + id +
                ", patientID='" + patientID + '\'' +
                ", doctorID='" + doctorID + '\'' +
                ", doctorFee='" + doctorFee + '\'' +
                ", appointmentTime='" + appointmentTime + '\'' +
                ", appointmentDate='" + appointmentDate + '\'' +
                '}';
    }
}
