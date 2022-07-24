package com.example.doctorscarespringbootapplication.controller.patient;

import com.example.doctorscarespringbootapplication.dao.*;
import com.example.doctorscarespringbootapplication.entity.AppointDoctor;
import com.example.doctorscarespringbootapplication.entity.Prescription;
import com.example.doctorscarespringbootapplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/patient")
public class PatientMainController {
    private final UserRepository userRepository;

    private final AppointDoctorRepository appointDoctorRepository;

    private final PrescriptionRepository prescriptionRepository;

    private final PostsRepository postsRepository;
    private final SavedPostsRepository savedPostsRepository;


    public PatientMainController(UserRepository userRepository, AppointDoctorRepository appointDoctorRepository, PrescriptionRepository prescriptionRepository, PostsRepository postsRepository, SavedPostsRepository savedPostsRepository) {
        this.userRepository = userRepository;
        this.appointDoctorRepository = appointDoctorRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.postsRepository = postsRepository;
        this.savedPostsRepository = savedPostsRepository;
    }

    @GetMapping("/index")
    public String patientHome(Model model, Principal principal) {
        model.addAttribute("title", "Patient Dashboard");
        User user = userRepository.getUserByEmailNative(principal.getName());
//        Three cards data
        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        String todayDate = dtfDate.format(now);
        String todaysAppointmentCount = appointDoctorRepository.countAllByAppointmentDateAndPatientID(todayDate, principal.getName());
        String todaysCompletedAppointment = prescriptionRepository.countAllByAppointDoctorAppointmentDateAndMedicinesIsNotNullAndAppointDoctorPatientID(todayDate, user.getId()+"");
        model.addAttribute("todaysAppointment", todaysAppointmentCount);
        model.addAttribute("todaysCompletedAppointment", todaysCompletedAppointment);

        String todaysGivenPrescriptions = prescriptionRepository.countAllByAppointDoctorAppointmentDateAndMedicinesIsNotNullAndAppointDoctorPatientID(todayDate, user.getId()+"");
        String totalPrescriptions = prescriptionRepository.countAllByAppointDoctorPatientID(user.getId()+"");
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

        List<AppointDoctor> appointDoctorList = appointDoctorRepository.findAllByAppointmentDateAndPatientIDAndAppointmentTimeGreaterThanOrderByAppointmentTimeAsc(todayDate, principal.getName(), currentTimeMinus30);
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
        return "patient/patient_home";
    }

    @GetMapping("/profile")
    public String patientProfile(Model model, Principal principal) {
        model.addAttribute("title", "Patient Profile");
        addCommonData(model, principal);
        return "patient/patient_profile";
    }

    @GetMapping("/appoint-doctor-type")
    public String patientAppointDoctor(Model model, Principal principal) {
        model.addAttribute("title", "Patient Dashboard");
        addCommonData(model, principal);
        return "patient/patient_appoint_doctor_types";
    }

    @GetMapping("/meet-doctor")
    public String patientMeetDoctor(Model model, Principal principal) throws ParseException {
        model.addAttribute("title", "Meet Doctor");
        model.addAttribute("senderEmail", principal.getName());

        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        String todayDate = dtfDate.format(now);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime value = localDateTime.minus(30, ChronoUnit.MINUTES);
        Time currentTimeMinus30 = Time.valueOf(dateTimeFormatter.format(value));

        List<AppointDoctor> appointDoctorList = this.appointDoctorRepository.findAllByAppointmentDateAndPatientIDAndAppointmentTimeGreaterThanOrderByAppointmentTimeAsc(todayDate, principal.getName(), currentTimeMinus30);
        if (appointDoctorList.size() != 0) {
            AppointDoctor appointDoctor = appointDoctorList.get(0);
            User user = userRepository.findById(Integer.parseInt(appointDoctor.getDoctorID()));
            model.addAttribute("doctorUser", user);
            model.addAttribute("receiverEmail", user.getEmail());
            model.addAttribute("appointmentID", appointDoctor.getId());

            User patientUser = userRepository.getUserByEmailNative(principal.getName());

            model.addAttribute("patientProfileImage", patientUser.getImageURL());
            model.addAttribute("doctorProfileImage", user.getImageURL());

            appointmentCountDown(model, dateTimeFormatter, localDateTime, appointDoctor);

        } else {
            model.addAttribute("noDoctorAppointment", "true");
        }


        addCommonData(model, principal);
        return "patient/patient_meet_doctor";
    }

    @GetMapping("/todays-appointment")
    public String patientTodayAppointment(Model model, Principal principal) {
        model.addAttribute("title", "Today's Appointment");

        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        String todayDate = dtfDate.format(now);

        List<AppointDoctor> appointDoctorList = appointDoctorRepository.findAllByAppointmentDateAndPatientIDOrderByAppointmentTimeAsc(todayDate, principal.getName());

        if (appointDoctorList.size() != 0) {
            model.addAttribute("appointDoctorList", appointDoctorList);
        } else {
            model.addAttribute("noDoctorAppointment", "true");
        }
        addCommonData(model, principal);

        return "patient/patient_today's_appointment";
    }


    @GetMapping("/view-prescriptions/{page}")
    public String viewPrescriptions(@PathVariable("page") Integer page, Model model, Principal principal) {
        model.addAttribute("title", "View Prescriptions");
        Pageable pageable = PageRequest.of(page-1, 8);
        Page<Prescription> prescriptionList = prescriptionRepository.findByAppointDoctorPatientIDAndSymptomsNotNullOrderByIdDesc(principal.getName(), pageable);
        if (prescriptionList.getTotalElements() == 0) {
            model.addAttribute("noPrescription", "true");
        }
        model.addAttribute("prescriptionList", prescriptionList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", prescriptionList.getTotalPages());
        addCommonData(model, principal);
        return "patient/patient_view_prescriptions";
    }

    @PostMapping("/view-single-prescription")
    public String viewSinglePrescription(@RequestParam String appointmentID, Model model, Principal principal) {
        model.addAttribute("title", "Single Prescription");
        model.addAttribute("appointmentID", appointmentID);

        AppointDoctor appointDoctor = appointDoctorRepository.findById(Integer.parseInt(appointmentID));
        User doctorUser = userRepository.findById(Integer.parseInt(appointDoctor.getDoctorID()));
        model.addAttribute("appointDoctor", appointDoctor);
        model.addAttribute("doctorUser", doctorUser);

        addCommonData(model, principal);
        return "patient/patient_view_single_prescription";
    }

    @GetMapping(value = "/uploadedImages/{filename}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> download(@PathVariable String filename) throws IOException {
        var data = Files.readAllBytes(Paths.get(  "upload/" + filename));
        return ResponseEntity.ok()
                .contentLength(data.length)
                .body(data);
    }


    public static void appointmentCountDown(Model model, DateTimeFormatter dateTimeFormatter, LocalDateTime localDateTime, AppointDoctor appointDoctor) throws ParseException {
        String currentTime = dateTimeFormatter.format(localDateTime);
        Time appointmentTime = appointDoctor.getAppointmentTime();
        String appointmentTimeStr = appointmentTime.toString();


        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date1 = format.parse(currentTime);
        Date date2 = format.parse(appointmentTimeStr);
        long difference = date2.getTime() - date1.getTime();
        long countDownTime = (difference/1000)/60;
        if (countDownTime <= 0) {
            countDownTime = 0;
        }
        model.addAttribute("appointCountdownTime", countDownTime);
    }


    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userEmail = principal.getName();
        User user = userRepository.getUserByEmailNative(userEmail);
        model.addAttribute("user", user);
    }
}
