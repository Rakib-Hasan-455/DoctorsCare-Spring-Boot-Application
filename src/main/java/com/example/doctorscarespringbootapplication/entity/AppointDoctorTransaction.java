package com.example.doctorscarespringbootapplication.entity;

import javax.persistence.*;
import java.sql.Time;

@Entity
public class AppointDoctorTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String txid;

    private String doctorId;

    private String doctorFee;

    @Basic
    private Time appointmentTime;

    @Column
    private String transactionStatus;

    public AppointDoctorTransaction(long id, String txid, String doctorId, String doctorFee, String transactionStatus) {
        this.id = id;
        this.txid = txid;
        this.doctorId = doctorId;
        this.doctorFee = doctorFee;
        this.transactionStatus = transactionStatus;
    }

    public AppointDoctorTransaction(String txid, String doctorId, String doctorFee, Time appointmentTime, String transactionStatus) {
        this.txid = txid;
        this.doctorId = doctorId;
        this.doctorFee = doctorFee;
        this.appointmentTime = appointmentTime;
        this.transactionStatus = transactionStatus;
    }

    public AppointDoctorTransaction() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Time getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Time appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorFee() {
        return doctorFee;
    }

    public void setDoctorFee(String doctorFee) {
        this.doctorFee = doctorFee;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
}
