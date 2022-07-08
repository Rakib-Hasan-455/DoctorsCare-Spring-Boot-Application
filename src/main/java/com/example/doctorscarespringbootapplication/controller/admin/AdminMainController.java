package com.example.doctorscarespringbootapplication.controller.admin;


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
@RequestMapping("/admin")
public class AdminMainController {

    @GetMapping("/index")
    public String adminHome(Model model, Principal principal) {
        model.addAttribute("title", "Admin Dashboard");
        addCommonData(model, principal);
        return "patient/admin_home";
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
