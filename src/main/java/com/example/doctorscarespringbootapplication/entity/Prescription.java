package com.example.doctorscarespringbootapplication.entity;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Prescription {

    @Id
    private int id;

    @ColumnDefault("")
    private String symptoms;

    @ColumnDefault("")
    private String tests;

    @ColumnDefault("")
    private String advice;

    @ColumnDefault("")
    private String medicines;

    @OneToOne
    private AppointDoctor appointDoctor;

    public Prescription() {
    }

    public Prescription(int id, String symptoms, String tests, String advice, String medicines, AppointDoctor appointDoctor) {
        this.id = id;
        this.symptoms = symptoms;
        this.tests = tests;
        this.advice = advice;
        this.medicines = medicines;
        this.appointDoctor = appointDoctor;
    }

    public Prescription(String symptoms, String tests, String advice, String medicines) {
        this.symptoms = symptoms;
        this.tests = tests;
        this.advice = advice;
        this.medicines = medicines;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getTests() {
        return tests;
    }

    public void setTests(String tests) {
        this.tests = tests;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public String getMedicines() {
        return medicines;
    }

    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }

    public AppointDoctor getAppointDoctor() {
        return appointDoctor;
    }

    public void setAppointDoctor(AppointDoctor appointDoctor) {
        this.appointDoctor = appointDoctor;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "id=" + id +
                ", symptoms='" + symptoms + '\'' +
                ", tests='" + tests + '\'' +
                ", advice='" + advice + '\'' +
                ", medicines='" + medicines + '\'' +
                ", appointDoctor=" + appointDoctor +
                '}';
    }
}
