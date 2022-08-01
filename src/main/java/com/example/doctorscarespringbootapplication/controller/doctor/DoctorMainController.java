package com.example.doctorscarespringbootapplication.controller.doctor;


import com.example.doctorscarespringbootapplication.controller.patient.PatientMainController;
import com.example.doctorscarespringbootapplication.dao.*;
import com.example.doctorscarespringbootapplication.entity.AppointDoctor;
import com.example.doctorscarespringbootapplication.entity.Prescription;
import com.example.doctorscarespringbootapplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/doctor")
public class DoctorMainController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppointDoctorRepository appointDoctorRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private SavedPostsRepository savedPostsRepository;

    @GetMapping("/index")
    public String doctorHome(Model model, Principal principal) {
        model.addAttribute("title", "Doctors Dashboard");
        User user = userRepository.getUserByEmailNative(principal.getName());
//        Three cards data
        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDateTime now = LocalDateTime.now();
        String todayDate = dtfDate.format(now);
        String todaysAppointmentCount = appointDoctorRepository.countAllByAppointmentDateAndDoctorID(Date.valueOf(todayDate), user.getId()+"");
        String todaysCompletedAppointment = prescriptionRepository.countAllByAppointDoctorAppointmentDateAndMedicinesIsNotNullAndAppointDoctorDoctorID(Date.valueOf(todayDate), user.getId()+"");
        model.addAttribute("todaysAppointment", todaysAppointmentCount);
        model.addAttribute("todaysCompletedAppointment", todaysCompletedAppointment);

        String todaysGivenPrescriptions = prescriptionRepository.countAllByAppointDoctorAppointmentDateAndMedicinesIsNotNullAndAppointDoctorDoctorID(Date.valueOf(todayDate), user.getId()+"");
        String totalPrescriptions = prescriptionRepository.countAllByAppointDoctorDoctorID(user.getId()+"");
        model.addAttribute("todaysGivenPrescriptions", todaysGivenPrescriptions);
        model.addAttribute("totalGivenPrescriptions", totalPrescriptions);

        long totalPosts = postsRepository.count();
        long totalSavedPosts = savedPostsRepository.countBySaverId(user.getId()+"");
        model.addAttribute("totalPosts", totalPosts);
        model.addAttribute("totalSavedPosts", totalSavedPosts);
//        Upcoming Appointments
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime value = localDateTime.minus(30, ChronoUnit.MINUTES);
        Time currentTimeMinus30 = Time.valueOf(dateTimeFormatter.format(value));

        List<AppointDoctor> appointDoctorList = appointDoctorRepository.findAllByAppointmentDateAndPatientIDAndAppointmentTimeGreaterThanOrderByAppointmentTimeAsc(Date.valueOf(todayDate), principal.getName(), currentTimeMinus30);
        if (appointDoctorList.size() != 0) {
            model.addAttribute("appointDoctorList", appointDoctorList);
        } else {
            model.addAttribute("noDoctorAppointment", "true");
        }

//        Top 3 doctors
        List<User> userList = new ArrayList<>();
        List<String> top3DoctorsList = appointDoctorRepository.findTop3DoctorsNativeQuery();
        for (String s : top3DoctorsList) {
            User userX = userRepository.findById(Integer.parseInt(s));
            userList.add(userX);
        }
        model.addAttribute("userList", userList);
        addCommonData(model, principal);
        return "doctor/doctor_home";
    }

    @GetMapping("/profile")
    public String patientProfile(Model model, Principal principal) {
        model.addAttribute("title", "Doctor Profile");
        addCommonData(model, principal);
        return "doctor/doctor_profile";
    }

    @GetMapping("/meet-patient")
    public String doctorMeetPatient(Model model, Principal principal) throws ParseException {
        model.addAttribute("title", "Meet Patient");

        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDateTime now = LocalDateTime.now();
        String todaysDate = dtfDate.format(now);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime value = localDateTime.minus(30, ChronoUnit.MINUTES);
        Time currentTimeMinus30 = Time.valueOf(dateTimeFormatter.format(value));

        User userDoctor = this.userRepository.getUserByEmailNative(principal.getName());
        String doctorsID = userDoctor.getId()+"";

        List<AppointDoctor> appointDoctorList = this.appointDoctorRepository.findAllByAppointmentDateAndDoctorIDAndAppointmentTimeGreaterThanOrderByAppointmentTimeAsc(Date.valueOf(todaysDate), doctorsID, currentTimeMinus30);
        if (appointDoctorList.size() != 0) {
            AppointDoctor appointDoctor = appointDoctorList.get(0);
            model.addAttribute("senderEmailDoctor", principal.getName());
            model.addAttribute("receiverEmailDoctor", appointDoctor.getPatientID());
            model.addAttribute("appointmentID", appointDoctor.getId());

            User userPatient = this.userRepository.getUserByEmailNative(appointDoctor.getPatientID());
            model.addAttribute("patientUser", userPatient);
            model.addAttribute("patientProfileImage", userPatient.getImageURL());
            model.addAttribute("doctorProfileImage", userDoctor.getImageURL());
            PatientMainController.appointmentCountDown(model, dateTimeFormatter, localDateTime, appointDoctor);
        } else {
            model.addAttribute("noDoctorAppointment", "true");
        }

        addCommonData(model, principal);
        return "doctor/doctor_meet_patient";
    }

    @GetMapping("/todays-appointment")
    public String patientTodayAppointment(Model model, Principal principal) {
        model.addAttribute("title", "Today's Appointment");

        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDateTime now = LocalDateTime.now();
        String todayDate = dtfDate.format(now);

        User user = userRepository.getUserByEmailNative(principal.getName());
        List<AppointDoctor> appointDoctorList = appointDoctorRepository
                .findAllByAppointmentDateAndDoctorIDOrderByAppointmentTimeAsc
                        (Date.valueOf(todayDate), user.getId()+"");


        if (appointDoctorList.size() != 0) {
            model.addAttribute("appointDoctorList", appointDoctorList);
        } else {
            model.addAttribute("noDoctorAppointment", "true");
        }
        addCommonData(model, principal);
        return "doctor/doctor_today's_appointment";
    }

    @GetMapping("/view-prescriptions/{page}")
    public String viewPrescriptions(@PathVariable("page") Integer page, Model model, Principal principal) {
        model.addAttribute("title", "View Prescriptions");
        Pageable pageable = PageRequest.of(page-1, 8);
        User user = this.userRepository.getUserByEmailNative(principal.getName());
        Page<Prescription> prescriptionList = prescriptionRepository
                .findByAppointDoctorDoctorIDAndSymptomsNotNullOrderByIdDesc(user.getId()+"", pageable);
        if (prescriptionList.getTotalElements() == 0) {
            model.addAttribute("noPrescription", "true");
        }
        model.addAttribute("prescriptionList", prescriptionList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", prescriptionList.getTotalPages());
        addCommonData(model, principal);
        return "doctor/doctor_view_prescriptions";
    }

    @PostMapping("/view-single-prescription")
    public String viewSinglePrescription(@RequestParam String appointmentID, Model model, Principal principal) {
        model.addAttribute("title", "Single Prescription");
        model.addAttribute("appointmentID", appointmentID);
        AppointDoctor appointDoctor = appointDoctorRepository.findById(Integer.parseInt(appointmentID));
        User patientUser = userRepository.getUserByEmailNative(appointDoctor.getPatientID());
        model.addAttribute("appointDoctor", appointDoctor);
        model.addAttribute("patientUser", patientUser);
        addCommonData(model, principal);
        return "doctor/doctor_view_single_prescription";
    }


    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userEmail = principal.getName();
        User user = this.userRepository.getUserByEmailNative(userEmail);
        model.addAttribute("user", user);
    }
}
