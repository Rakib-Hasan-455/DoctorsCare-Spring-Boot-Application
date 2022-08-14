package com.example.doctorscarespringbootapplication.dto;

public class DoctorGivePrescriptionDTO {
    private String appointmentID = "";

    private String symptoms = "";

    private String tests = "";

    private String advice = "";

    private String medicines = "";

    public DoctorGivePrescriptionDTO(String appointmentID, String symptoms, String tests, String advice, String medicines) {
        this.appointmentID = appointmentID + "";
        this.symptoms = symptoms + "";
        this.tests = tests + "";
        this.advice = advice + "";
        this.medicines = medicines + "";
    }

    public DoctorGivePrescriptionDTO(String symptoms, String tests, String advice, String medicines) {
        this.symptoms = symptoms;
        this.tests = tests;
        this.advice = advice;
        this.medicines = medicines;
    }

    public DoctorGivePrescriptionDTO() {

    }

    public String getAppointmentID() {
        return appointmentID + "";
    }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID + "";
    }

    public String getSymptoms() {
        return symptoms + "";
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms + "";
    }

    public String getTests() {
        return tests + "";
    }

    public void setTests(String tests) {
        this.tests = tests + "";
    }

    public String getAdvice() {
        return advice + "";
    }

    public void setAdvice(String advice) {
        this.advice = advice + "";
    }

    public String getMedicines() {
        return medicines + "";
    }

    public void setMedicines(String medicines) {
        this.medicines = medicines + "";
    }
}
