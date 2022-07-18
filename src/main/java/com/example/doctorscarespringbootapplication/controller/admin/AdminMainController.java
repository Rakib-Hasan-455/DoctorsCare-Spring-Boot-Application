package com.example.doctorscarespringbootapplication.controller.admin;


import com.example.doctorscarespringbootapplication.dao.AppointDoctorRepository;
import com.example.doctorscarespringbootapplication.dao.UserRepository;
import com.example.doctorscarespringbootapplication.entity.AppointDoctor;
import com.example.doctorscarespringbootapplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminMainController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    AppointDoctorRepository appointDoctorRepository;

    @GetMapping("/index")
    public String adminHome(Model model, Principal principal) {
        model.addAttribute("title", "Admin Dashboard");
        addCommonData(model, principal);
        return "admin/admin_home";
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
            Model model, Principal principal) {
        System.out.println(sendMailTo);
        System.out.println(emailSubject);
        System.out.println(emailBody);
        model.addAttribute("title", "Email Sent Successful");
        model.addAttribute("emailSent", true);
        addCommonData(model, principal);
        return "admin/admin_email_service";
    }

    @GetMapping("/appointment-logs")
    public String patientTodayAppointment(Model model, Principal principal) {
        model.addAttribute("title", "Appointment Logs");
        List<AppointDoctor> appointDoctorList = appointDoctorRepository.findAllByOrderByIdDesc();
        if (appointDoctorList.size() != 0) {
            model.addAttribute("appointDoctorList", appointDoctorList);
        } else {
            model.addAttribute("noDoctorAppointment", "true");
        }
        addCommonData(model, principal);
        return "admin/admin_appointment_logs";
    }


    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userEmail = principal.getName();
        User user = this.userRepository.getUserByEmailNative(userEmail);
        model.addAttribute("user", user);
    }
}
