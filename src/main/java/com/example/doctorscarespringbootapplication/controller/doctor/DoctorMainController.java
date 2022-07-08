package com.example.doctorscarespringbootapplication.controller.doctor;


import com.example.doctorscarespringbootapplication.dao.UserRepository;
import com.example.doctorscarespringbootapplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/doctor")
public class DoctorMainController {
    @GetMapping("/index")
    public String doctorHome(Model model, Principal principal) {
        model.addAttribute("title", "Doctors Dashboard");
        addCommonData(model, principal);
        return "doctor/doctor_home";
    }

    @GetMapping("/meet-doctor")
    public String patientMeetDoctor(Model model, Principal principal) {
        model.addAttribute("title", "Meet Doctor");
        model.addAttribute("senderEmailDoctor", principal.getName());
        addCommonData(model, principal);
        return "doctor/doctor_meet_doctor";
    }

    @Autowired
    private UserRepository userRepository;
    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userEmail = principal.getName();
        System.out.println("Email = "+userEmail);
        User user = this.userRepository.getUserByEmailNative(userEmail);
        model.addAttribute("user", user);
    }
}
