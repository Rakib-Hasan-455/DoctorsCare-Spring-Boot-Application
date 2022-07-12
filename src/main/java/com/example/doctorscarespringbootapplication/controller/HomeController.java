package com.example.doctorscarespringbootapplication.controller;

import com.example.doctorscarespringbootapplication.dao.AppointDoctorRepository;
import com.example.doctorscarespringbootapplication.dao.PrescriptionRepository;
import com.example.doctorscarespringbootapplication.dao.UserRepository;
import com.example.doctorscarespringbootapplication.dto.DoctorSignup;
import com.example.doctorscarespringbootapplication.entity.DoctorsAdditionalInfo;
import com.example.doctorscarespringbootapplication.entity.DoctorsSchedule;
import com.example.doctorscarespringbootapplication.entity.User;
import com.example.doctorscarespringbootapplication.ssl.commerz.SSLCommerz;
import com.example.doctorscarespringbootapplication.ssl.commerz.Utility.ParameterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.Map;
import java.util.Objects;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointDoctorRepository appointDoctorRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home | Doctors Care");
        return "index.html";
    }


    @GetMapping("/terms-and-conditions")
    public String termsAndConditions(Model model) {
        model.addAttribute("title", "Terms & Conditions");
        return "terms-and-conditions.html";
    }

    @GetMapping("/patient-signup")
    public String patientSignup(Model model) {
        model.addAttribute("title", "Patient | Patient Signup");
        model.addAttribute("user", new User());
        return "patient_signup";
    }

    @PostMapping("/process-patient-signup")
    public String patientSignupProcess(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model) {
        user.setRole("ROLE_PATIENT");
        user.setEnabled(true);
        user.setImageURL("default.jpg");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        System.out.println(user);
        if (this.userRepository.getUserByEmailNative(user.getEmail()) != null) {
            System.out.println("User Already Registered With this Email");
            bindingResult.rejectValue("email", "error.user", "An account already exists for this email.");
        }

        if (bindingResult.hasErrors()) {
//            System.out.println(bindingResult);
            model.addAttribute("title", "Patient | Patient Signup");
            return "patient_signup";
        }
        model.addAttribute("successMsg", "successfully signed up. Please Login!");
        model.addAttribute("successMsgType", "alert-success");
        this.userRepository.save(user);
        return "patient_signup";
    }

    @GetMapping("/doctor-signup")
    public String doctorSignup(Model model) {
        model.addAttribute("title", "Doctor | Doctor Signup");
//        model.addAttribute("user", new DoctorsAdditionalInfo());
        model.addAttribute("user", new DoctorSignup());
        return "doctor_signup";
    }

    @PostMapping("process-doctor-signup")
    public String doctorSignupProcess(@Valid @ModelAttribute DoctorSignup doctorSignup, BindingResult bindingResult, Model model) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = new User(doctorSignup.getName(), doctorSignup.getEmail(), doctorSignup.getPassword(), doctorSignup.getAbout(), doctorSignup.getDOB(), doctorSignup.getPhone(), doctorSignup.getAddress());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_DOCTOR");
        user.setEnabled(true);
        user.setImageURL("defaultDoctor.jpg");
        DoctorsAdditionalInfo doctorsAdditionalInfo = new DoctorsAdditionalInfo(doctorSignup.getNid(), doctorSignup.getDoctortype(), doctorSignup.getDegrees(), doctorSignup.getMedicalcollege(), doctorSignup.getAppointmentfee());
        user.setDoctorsAdditionalInfo(doctorsAdditionalInfo);
        doctorsAdditionalInfo.setUser(user);
//        System.out.println(doctorSignup);
        DoctorsSchedule doctorsSchedule = new DoctorsSchedule(true, true, true, true, true, true, true, true, true);
        user.setDoctorsSchedule(doctorsSchedule);
        doctorsSchedule.setUser(user);
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        System.out.println(user);
//        System.out.println(doctorsAdditionalInfo);
//        System.out.println(doctorsSchedule);
        model.addAttribute("user", doctorSignup);
        if (this.userRepository.getUserByEmailNative(user.getEmail()) != null) {
            System.out.println("User Already Registered With this Email");
            bindingResult.rejectValue("email", "error.user", "An account already exists with this email.");
        }
        System.out.println(bindingResult);
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult);
            if (bindingResult.getFieldError("email") != null) {
                String emailError = Objects.requireNonNull(bindingResult.getFieldError("email")).getDefaultMessage() + "\n";
                model.addAttribute("emailError", "yes");
                model.addAttribute("emailErrorMsg", emailError);
            }
            if (bindingResult.getFieldError("password") != null) {
                String passError = Objects.requireNonNull(bindingResult.getFieldError("password")).getDefaultMessage() + "\n";
                model.addAttribute("passError", "yes");
                model.addAttribute("passErrorMsg", passError);
            }
            model.addAttribute("title", "Doctor | Doctor Signup");
            return "doctor_signup";
        }

        model.addAttribute("successMsg", "successfully signed up. Please Login!");
        model.addAttribute("successMsgType", "alert-success");
        this.userRepository.save(user);
//        this.userRepository.sa
        return "doctor_signup";
    }



    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/test-payment")
    public RedirectView payTest() throws Exception {
        Map<String, String>  transactionMap = ParameterBuilder.constructRequestParameters();
        SSLCommerz sslCommerz = new SSLCommerz("abc62bdc6438f3c5", "abc62bdc6438f3c5@ssl", true);
        String url = sslCommerz.initiateTransaction(transactionMap, false);
//        System.out.println("The url: " + url);
//        System.out.println("after previous url");
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);
        return redirectView;
    }

    @GetMapping("/test-payment-success")

    public String payTestSuccess() {
        System.out.println("Dummy was called");
        return "dummy";
    }




}
