package com.example.doctorscarespringbootapplication.controller.patient;

import com.example.doctorscarespringbootapplication.dao.AdditionalDoctorsRepository;
import com.example.doctorscarespringbootapplication.dao.AppointDoctorRepository;
import com.example.doctorscarespringbootapplication.dao.UserRepository;
import com.example.doctorscarespringbootapplication.entity.AppointDoctor;
import com.example.doctorscarespringbootapplication.entity.DoctorsAdditionalInfo;
import com.example.doctorscarespringbootapplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/patient")
public class PatientMainController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdditionalDoctorsRepository additionalDoctorsRepository;

    @Autowired
    private AppointDoctorRepository appointDoctorRepository;

    @GetMapping("/index")
    public String patientHome(Model model, Principal principal) {
        model.addAttribute("title", "Patient Dashboard");
        addCommonData(model, principal);
        return "patient/patient_home";
    }

    @GetMapping("/appoint-doctor-type")
    public String patientAppointDoctor(Model model, Principal principal) {
        model.addAttribute("title", "Patient Dashboard");
        addCommonData(model, principal);
        return "patient/patient_appoint_doctor_types";
    }

    @GetMapping("/meet-doctor")
    public String patientMeetDoctor(Model model, Principal principal) {
        model.addAttribute("title", "Meet Doctor");
        model.addAttribute("senderEmail", principal.getName());
        addCommonData(model, principal);
        return "patient/patient_meet_doctor";
    }

    @GetMapping("/todays-appointment")
    public String patientTodaysAppointment(Model model, Principal principal) {
        model.addAttribute("title", "Todays Appointment");

        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        String todaysDate = dtfDate.format(now);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime value = localDateTime.minus(30, ChronoUnit.MINUTES);
        Time currentTimeMinus30 = Time.valueOf(dateTimeFormatter.format(value));

        System.out.println(currentTimeMinus30.toString());
        List<AppointDoctor> appointDoctorList = this.appointDoctorRepository.findAllByAppointmentDateAndPatientIDAndAppointmentTimeGreaterThanOrderByAppointmentTimeAsc(todaysDate, principal.getName(), currentTimeMinus30);
        model.addAttribute("appointDoctorList", appointDoctorList);
        addCommonData(model, principal);
        return "patient/patient_todays_appointment";
    }

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userEmail = principal.getName();
        System.out.println("Email = "+userEmail);
        User user = this.userRepository.getUserByEmailNative(userEmail);
        System.out.println(user);
        model.addAttribute("user", user);
    }
}
