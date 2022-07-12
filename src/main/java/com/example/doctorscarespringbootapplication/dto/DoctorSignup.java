package com.example.doctorscarespringbootapplication.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class DoctorSignup {
    private String name;
    @NotBlank(message = "Email can't be blank")
    @Pattern(regexp = "[a-z0-9]+.+@[a-z]+\\.[a-z]{2,3}", message = "Email is not valid!")
//    @Pattern(regexp = "[a-z0-9]+^[1-9]\\d*(\\.\\d+)+@[a-z]+\\.[a-z]{2,3}", message = "Email is not valid!")

    private String email;

    @NotBlank(message = "Field Can't be blank")
//    @Pattern(regexp = "\"^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{3,}$\"\n", message = "Password Should Be of Minimum eight characters, at least one letter and one number")
    @Size(min = 3, max = 250, message = "Password must be within 3 - 250 characters!")
    private String password;
    private String about;
    private String role;
    private boolean enabled;
    private String imageURL;

    //    Date of birth
    private String DOB;

    private String phone;

    private String address;

    private String nid;

    private String doctortype;

    private String degrees;

    private String medicalcollege;

    private String appointmentfee;


    public DoctorSignup(String name, String email, String password, String about, String role, boolean enabled, String imageURL, String DOB, String phone, String address, String nid, String doctortype, String degrees, String medicalcollege, String appointmentfee) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.about = about;
        this.role = role;
        this.enabled = enabled;
        this.imageURL = imageURL;
        this.DOB = DOB;
        this.phone = phone;
        this.address = address;
        this.nid = nid;
        this.doctortype = doctortype;
        this.degrees = degrees;
        this.medicalcollege = medicalcollege;
        this.appointmentfee = appointmentfee;
    }

    public DoctorSignup() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
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



    @Override
    public String toString() {
        return "DoctorSignup{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", about='" + about + '\'' +
                ", role='" + role + '\'' +
                ", enabled=" + enabled +
                ", imageURL='" + imageURL + '\'' +
                ", DOB='" + DOB + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", nid='" + nid + '\'' +
                ", doctortype='" + doctortype + '\'' +
                ", degrees='" + degrees + '\'' +
                ", medicalcollege='" + medicalcollege + '\'' +
                ", appointmentfee='" + appointmentfee + '\'' +
                '}';
    }
}
