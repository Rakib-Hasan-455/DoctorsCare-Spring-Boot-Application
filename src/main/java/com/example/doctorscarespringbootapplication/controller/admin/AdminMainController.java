package com.example.doctorscarespringbootapplication.controller.admin;


import com.example.doctorscarespringbootapplication.configuration.emailSender.EmailSenderServiceJava;
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

import javax.mail.MessagingException;
import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminMainController {
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
    public String adminHome(Model model, Principal principal) {
        model.addAttribute("title", "Admin Dashboard");
//        Three cards data
        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDateTime now = LocalDateTime.now();
        String todayDate = dtfDate.format(now);
        String todaysAppointmentCount = appointDoctorRepository.countAllByAppointmentDate(Date.valueOf(todayDate));
        String todaysCompletedAppointment = prescriptionRepository.countAllByAppointDoctorAppointmentDateAndMedicinesIsNotNull(Date.valueOf(todayDate));
        model.addAttribute("todaysAppointment", todaysAppointmentCount);
        model.addAttribute("todaysCompletedAppointment", todaysCompletedAppointment);

        String todaysGivenPrescriptions = prescriptionRepository.countAllByAppointDoctorAppointmentDateAndMedicinesIsNotNull(Date.valueOf(todayDate));
        long totalPrescriptions = prescriptionRepository.count();
        model.addAttribute("todaysGivenPrescriptions", todaysGivenPrescriptions);
        model.addAttribute("totalGivenPrescriptions", totalPrescriptions);

        long totalPatient = userRepository.countByRole("ROLE_PATIENT");
        long totalActivePatient = userRepository.countByRoleAndEnabled("ROLE_PATIENT", true);
        model.addAttribute("totalPatient", totalPatient);
        model.addAttribute("totalActivePatient", totalActivePatient);

        long totalDoctor = userRepository.countByRole("ROLE_DOCTOR");
        long totalActiveDoctor = userRepository.countByRoleAndEnabled("ROLE_DOCTOR", true);
        model.addAttribute("totalDoctor", totalDoctor);
        model.addAttribute("totalActiveDoctor", totalActiveDoctor);
//        Upcoming Appointments
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime value = localDateTime.minus(30, ChronoUnit.MINUTES);
        Time currentTimeMinus30 = Time.valueOf(dateTimeFormatter.format(value));

        Pageable pageable = PageRequest.of(0, 10);
        Page<AppointDoctor> appointDoctorList = appointDoctorRepository.findAllByAppointmentDateOrderByIdDesc(Date.valueOf(todayDate), pageable);
        if (appointDoctorList.getTotalElements() != 0) {
            model.addAttribute("appointDoctorList", appointDoctorList);
        } else {
            model.addAttribute("noDoctorAppointment", "true");
        }

//        Top 3 doctors
        List<User> userList = new ArrayList<>();
        List<String> top3DoctorsList = appointDoctorRepository.findTop3DoctorsNativeQuery();
        for (String s : top3DoctorsList) {
            User userX = userRepository.findById(Integer.parseInt(s));
            if (userX != null) {
                userList.add(userX);
            }
        }
        model.addAttribute("userList", userList);
        addCommonData(model, principal);
        return "admin/admin_home";
    }

    @GetMapping("/profile")
    public String patientProfile(Model model, Principal principal) {
        model.addAttribute("title", "Patient Profile");
        addCommonData(model, principal);
        return "admin/admin_profile";
    }

    @GetMapping("/emailing-service")
    public String adminEmailingService(Model model, Principal principal) {
        model.addAttribute("title", "Admin Emailing Service");
        addCommonData(model, principal);
        return "admin/admin_email_service";
    }

    @PostMapping("/process-email-send")
    public String emailServiceProcess(@RequestParam("sendMailTo") String sendMailTo,
                                      @RequestParam("emailSubject") String emailSubject,
                                      @RequestParam("emailBody") String emailBody,
                                      Model model, Principal principal) throws MessagingException {
        System.out.println(sendMailTo);
        System.out.println(emailSubject);
        System.out.println(emailBody);

        switch (sendMailTo) {
            case "All Patients":
                sendMailToAllPatients(emailSubject, emailBody);
                break;
            case "All Doctors":
                sendMailToAllDoctors(emailSubject, emailBody);
                break;
            case "All Patients & Doctors":
                sendMailToAllPatientsAndDoctors(emailSubject, emailBody);
                break;
        }
        model.addAttribute("title", "Email Sent Successful");
        model.addAttribute("emailSent", true);
        addCommonData(model, principal);
        return "admin/admin_email_service";
    }

    @Autowired
    private EmailSenderServiceJava emailSenderServiceJava;

    private void sendMailToAllPatients(String emailSubject, String emailBody) throws MessagingException {
        List<User> allPatientEmailList = userRepository.findByRoleAndEnabled("ROLE_PATIENT", true);
        for (User user : allPatientEmailList) {
            emailSenderServiceJava.sendEmail(user.getEmail(), emailSubject, emailBody);
        }

    }

    private void sendMailToAllDoctors(String emailSubject, String emailBody) throws MessagingException {
        List<User> allDoctorEmailList = userRepository.findByRoleAndEnabled("ROLE_DOCTOR", true);
        for (User user : allDoctorEmailList) {
            emailSenderServiceJava.sendEmail(user.getEmail(), emailSubject, emailBody);
        }
    }

    private void sendMailToAllPatientsAndDoctors(String emailSubject, String emailBody) throws MessagingException {
        List<User> allPatientAndDoctorEmailList = userRepository.findByRoleAndEnabledNative("ROLE_PATIENT", "ROLE_DOCTOR");
        for (User user : allPatientAndDoctorEmailList) {
            emailSenderServiceJava.sendEmail(user.getEmail(), emailSubject, emailBody);
        }
    }

    @GetMapping("/appointment-logs/{page}")
    public String patientTodayAppointment(@PathVariable("page") Integer page, Model model, Principal principal) {
        model.addAttribute("title", "Appointment Logs");
        Pageable pageable = PageRequest.of(page - 1, 8);
        Page<AppointDoctor> appointDoctorList = appointDoctorRepository.findAllByOrderByIdDesc(pageable);
        System.out.println(appointDoctorList.getTotalElements());
        if (appointDoctorList.getTotalElements() != 0) {
            model.addAttribute("appointDoctorList", appointDoctorList);
        } else {
            model.addAttribute("noDoctorAppointment", "true");
        }
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", appointDoctorList.getTotalPages());
        addCommonData(model, principal);
        return "admin/admin_appointment_logs";
    }

    @GetMapping("/prescription-logs/{page}")
    public String prescriptionLogs(@PathVariable("page") Integer page, Model model, Principal principal) {
        model.addAttribute("title", "Appointment Logs");
        Pageable pageable = PageRequest.of(page - 1, 8);
        Page<Prescription> prescriptionList = prescriptionRepository
                .findAllByMedicinesNotNullOrderByIdDesc(pageable);
        if (prescriptionList.getTotalElements() == 0) {
            model.addAttribute("noPrescription", "true");
        }
        model.addAttribute("prescriptionList", prescriptionList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", prescriptionList.getTotalPages());
        addCommonData(model, principal);
        return "admin/admin_prescription_logs";
    }

    @PostMapping("/view-single-prescription")
    public String viewSinglePrescription(@RequestParam String appointmentID, Model model, Principal principal) {
        model.addAttribute("title", "Viewing Prescription");
        model.addAttribute("appointmentID", appointmentID);
        AppointDoctor appointDoctor = appointDoctorRepository.findById(Integer.parseInt(appointmentID));
        User patientUser = userRepository.getUserByEmailNative(appointDoctor.getPatientID());
        User doctorUser = userRepository.findById(Integer.parseInt(appointDoctor.getDoctorID()));
        model.addAttribute("appointDoctor", appointDoctor);
        model.addAttribute("patientUser", patientUser);
        model.addAttribute("doctorUser", doctorUser);
        addCommonData(model, principal);
        return "admin/admin_view_single_prescription";
    }

    @GetMapping("/patients-list/{page}")
    public String patientsList(@PathVariable("page") Integer page, Model model, Principal principal) {
        model.addAttribute("title", "Patients List");
        Pageable pageable = PageRequest.of(page - 1, 8);
        Page<User> patientList = userRepository.findByRoleOrderByIdDesc("ROLE_PATIENT", pageable);
        if (patientList.getTotalElements() == 0) {
            model.addAttribute("noPatient", true);
        }
        model.addAttribute("patientList", patientList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", patientList.getTotalPages());
        addCommonData(model, principal);
        return "admin/admin_patient_list";
    }

    @GetMapping("/doctors-list/{page}")
    public String doctorsList(@PathVariable("page") Integer page, Model model, Principal principal) {
        model.addAttribute("title", "Doctors List");
        Pageable pageable = PageRequest.of(page - 1, 8);
        Page<User> doctorList = userRepository.findByRoleOrderByIdDesc("ROLE_DOCTOR", pageable);
        if (doctorList.getTotalElements() == 0) {
            model.addAttribute("noDoctor", true);
        }
        model.addAttribute("doctorList", doctorList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", doctorList.getTotalPages());
        addCommonData(model, principal);
        return "admin/admin_doctor_list";
    }


    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userEmail = principal.getName();
        User user = this.userRepository.getUserByEmailNative(userEmail);
        model.addAttribute("user", user);
    }
}
