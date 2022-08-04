package com.example.doctorscarespringbootapplication.entity;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

//    @NotBlank(message = "Field Can't be blank")
//    @Size(min = 3, max = 30, message = "Username must be within 3 - 20 characters!")
    private String name;

    @Valid
    @Column(unique = true)
    @NotBlank(message = "Email can't be blank")
    @Pattern(regexp = "[a-z0-9]+.+@[a-z]+\\.[a-z]{2,3}", message = "Email is not valid!")
    private String email;

    @Valid
    @NotBlank(message = "Field Can't be blank")
//    @Pattern(regexp = "\"^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{3,}$\"\n", message = "Password Should Be of Minimum eight characters, at least one letter and one number")
    @Size(min = 5, max = 250, message = "Password must be within 5 - 250 characters!")
    private String password;

//    @Column(length = 255)
//    @NotBlank(message = "Field Can't be blank")
//    @Size(min = 3, max = 250, message = "About must be within 3 - 250 characters!")
    private String about;

    private String role;
    private boolean enabled;

    @Column(length = 10000)
    private String imageURL;

//    Date of birth
    private String DOB;

    private String phone;

    private String address;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<Posts> posts;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private DoctorsAdditionalInfo doctorsAdditionalInfo;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private DoctorsSchedule doctorsSchedule;

    public User() {
    }



    public User(int id, String name, String email, String password, String role, boolean enabled, String imageURL, String about) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
        this.imageURL = imageURL;
        this.about = about;
    }

    public User(int id, String name, String email, String password, String about, String role, boolean enabled, String imageURL, String DOB, String phone, String address, DoctorsAdditionalInfo doctorsAdditionalInfo) {
        this.id = id;
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
        this.doctorsAdditionalInfo = doctorsAdditionalInfo;
    }

    public User(String name, String email, String password, String about, String DOB, String phone, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.about = about;
//        this.role = role;
//        this.enabled = enabled;
//        this.imageURL = imageURL;
        this.DOB = DOB;
        this.phone = phone;
        this.address = address;
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

    public DoctorsAdditionalInfo getDoctorsAdditionalInfo() {
        return doctorsAdditionalInfo;
    }

    public void setDoctorsAdditionalInfo(DoctorsAdditionalInfo doctorsAdditionalInfo) {
        this.doctorsAdditionalInfo = doctorsAdditionalInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public List<Posts> getPosts() {
        return posts;
    }

    public void setPosts(List<Posts> posts) {
        this.posts = posts;
    }

    public DoctorsSchedule getDoctorsSchedule() {
        return doctorsSchedule;
    }

    public void setDoctorsSchedule(DoctorsSchedule doctorsSchedule) {
        this.doctorsSchedule = doctorsSchedule;
    }

    //    public List<Contact> getContactList() {
//        return contactList;
//    }
//
//    public void setContactList(List<Contact> contactList) {
//        this.contactList = contactList;
//    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", about='" + about + '\'' +
                ", role='" + role + '\'' +
                ", enabled=" + enabled +
                ", imageURL='" + imageURL + '\'' +
                ", DOB='" + DOB + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", doctorsAdditionalInfo=" + doctorsAdditionalInfo +
                ", doctorsSchedule=" + doctorsSchedule +
                '}';
    }
}
