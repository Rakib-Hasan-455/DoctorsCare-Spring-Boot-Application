package com.example.doctorscarespringbootapplication.dto;

import java.sql.Time;

public class AppointDoctorDTO {
    private Time appointmentTime;
    private String doctorID;
    private String doctorFee;

    public AppointDoctorDTO() {
    }

    public AppointDoctorDTO(Time appointmentTime, String doctorID, String doctorFee) {
        this.appointmentTime = appointmentTime;
        this.doctorID = doctorID;
        this.doctorFee = doctorFee;
    }

    public Time getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Time appointmentTime) {
        this.appointmentTime = appointmentTime;
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

    @Override
    public String toString() {
        return "AppointDoctorDTO{" +
                "appointmentTime='" + appointmentTime + '\'' +
                ", doctorID='" + doctorID + '\'' +
                ", doctorFee='" + doctorFee + '\'' +
                '}';
    }
}
