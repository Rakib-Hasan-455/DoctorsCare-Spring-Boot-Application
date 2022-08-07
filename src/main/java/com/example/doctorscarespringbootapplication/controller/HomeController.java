package com.example.doctorscarespringbootapplication.controller;

import com.example.doctorscarespringbootapplication.configuration.EmailSenderServiceJava;
import com.example.doctorscarespringbootapplication.dao.AccountActiveTokenRepository;
import com.example.doctorscarespringbootapplication.dao.AppointDoctorRepository;
import com.example.doctorscarespringbootapplication.dao.PrescriptionRepository;
import com.example.doctorscarespringbootapplication.dao.UserRepository;
import com.example.doctorscarespringbootapplication.dto.DoctorSignup;
import com.example.doctorscarespringbootapplication.entity.AccountActiveToken;
import com.example.doctorscarespringbootapplication.entity.DoctorsAdditionalInfo;
import com.example.doctorscarespringbootapplication.entity.DoctorsSchedule;
import com.example.doctorscarespringbootapplication.entity.User;
import com.example.doctorscarespringbootapplication.configuration.commerz.SSLCommerz;
import com.example.doctorscarespringbootapplication.configuration.commerz.Utility.ParameterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointDoctorRepository appointDoctorRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private EmailSenderServiceJava emailSenderServiceJava;

    @Autowired
    private AccountActiveTokenRepository accountActiveTokenRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Doctors Care | Home");
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
    public String patientSignupProcess(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model) throws IOException, MessagingException {
        user.setRole("ROLE_PATIENT");
        user.setEnabled(false);
        user.setImageURL(user.getImageURL());
        System.out.println(user.getImageURL());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("Image URL ->"+user.getImageURL());
        if (this.userRepository.getUserByEmailNative(user.getEmail()) != null) {
            bindingResult.rejectValue("email", "error.user", "An account already exists for this email.");
            if (!userRepository.getUserByEmailNative(user.getEmail()).isEnabled()) {
                bindingResult.rejectValue("email", "error.user", "Your account is not Enabled. Please check your email if you haven't verified your account yet.");
            }
        }

        if (user.getImageURL().equals("")) {
            bindingResult.rejectValue("imageURL", "error.user", "You must upload an Image.");
            model.addAttribute("imageUpload", false);
        }

        String token = getRandomTokenString();
        AccountActiveToken accountActiveToken = new AccountActiveToken();
        accountActiveToken.setToken(token);
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        accountActiveToken.setTokenGenerationDate(Date.valueOf(dateTimeFormatter.format(localDateTime)));
        accountActiveToken.setEmailIsVerified(false);
        accountActiveToken.setUser(user);
        user.setAccountActiveToken(accountActiveToken);

        if (accountActiveTokenRepository.isTokenAlreadyInRepositoryNative(token) != null) {
            bindingResult.rejectValue("email", "error.user", "Token Generate Failed. Please Try Again.");
            model.addAttribute("tokenGenerateFail", true);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Patient | Patient Signup");
            return "patient_signup";
        }

        model.addAttribute("successMsg", "Sign-Up Request Successful. Please Check Your Email To Active Your Account!");
        model.addAttribute("successMsgType", "alert-success");

        userRepository.save(user);

        emailSenderServiceJava.sendEmail(user.getEmail(), "DoctorsCare Account Verification",
                "<h4>Dear Patient, <br> To verify your account please click this link.<h4> <h3><a target=\"_blank\" href=\"http://localhost:8080/verify-account?token="+user.getAccountActiveToken().getToken()+"\"> Click Here To Active Your Account</a></h3>" +
                        "<h4>Regards,<br>" +
                        "Doctors Care Team</h4>");
        return "patient_signup";
    }

    @GetMapping("/doctor-signup")
    public String doctorSignup(Model model) {
        model.addAttribute("title", "Doctor | Doctor Signup");
        model.addAttribute("user", new DoctorSignup());
        return "doctor_signup";
    }

    @PostMapping("process-doctor-signup")
    public String doctorSignupProcess(@Valid @ModelAttribute DoctorSignup doctorSignup, BindingResult bindingResult, Model model) throws MessagingException {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = new User(doctorSignup.getName(), doctorSignup.getEmail(), doctorSignup.getPassword(), doctorSignup.getAbout(), doctorSignup.getDOB(), doctorSignup.getPhone(), doctorSignup.getAddress());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_DOCTOR");
        user.setEnabled(false);
        user.setImageURL(doctorSignup.getImageURL());
        System.out.println("Image URL ->"+user.getImageURL());
        DoctorsAdditionalInfo doctorsAdditionalInfo = new DoctorsAdditionalInfo(doctorSignup.getNid(), doctorSignup.getDoctortype(), doctorSignup.getDegrees(), doctorSignup.getMedicalcollege(), doctorSignup.getAppointmentfee());
        user.setDoctorsAdditionalInfo(doctorsAdditionalInfo);
        doctorsAdditionalInfo.setUser(user);
        DoctorsSchedule doctorsSchedule = new DoctorsSchedule(true, true, true, true, true, true, true, true, true);
        user.setDoctorsSchedule(doctorsSchedule);
        doctorsSchedule.setUser(user);
        model.addAttribute("user", doctorSignup);
        if (this.userRepository.getUserByEmailNative(user.getEmail()) != null) {
            bindingResult.rejectValue("email", "error.user", "An account already exists with this email.");
            if (!userRepository.getUserByEmailNative(user.getEmail()).isEnabled()) {
                bindingResult.rejectValue("email", "error.user", "Your account is not Enabled. Please check your email if you haven't verified your account yet.");
            }
            model.addAttribute("imageUpload", false);
        }

        if (doctorSignup.getImageURL().equals("")) {
            bindingResult.rejectValue("imageURL", "error.user", "You must upload an Image.");
        }

        String token = getRandomTokenString();
        AccountActiveToken accountActiveToken = new AccountActiveToken();
        accountActiveToken.setToken(token);
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        accountActiveToken.setTokenGenerationDate(Date.valueOf(dateTimeFormatter.format(localDateTime)));
        accountActiveToken.setEmailIsVerified(false);
        accountActiveToken.setUser(user);
        user.setAccountActiveToken(accountActiveToken);

        if (accountActiveTokenRepository.isTokenAlreadyInRepositoryNative(token) != null) {
            bindingResult.rejectValue("email", "error.user", "Token Generate Failed. Please Try Again.");
            model.addAttribute("tokenGenerateFail", true);
        }

        if (bindingResult.hasErrors()) {
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

        model.addAttribute("successMsg", "Sign-Up Request Successful. Please Check Your Email To Active Your Account!");
        model.addAttribute("successMsgType", "alert-success");

        userRepository.save(user);

        emailSenderServiceJava.sendEmail(user.getEmail(), "DoctorsCare Account Verification",
                "<h4>Dear Doctor, <br> To verify your account please click this link.<h4> <h3><a target=\"_blank\" href=\"http://localhost:8080/verify-account?token="+user.getAccountActiveToken().getToken()+"\"> Click Here To Active Your Account</a></h3>" +
                        "<h4>Regards,<br>" +
                        "Doctors Care Team</h4>");
        return "doctor_signup";
    }

    @GetMapping("/verify-account")
    public String verifyAccount(@RequestParam String token, Model model) {
        model.addAttribute("title", "Verify Account Status");
        AccountActiveToken accountActiveToken = accountActiveTokenRepository.getAccountActiveTokenByToken(token);
        if (accountActiveToken != null) {
            if (!accountActiveToken.isEmailIsVerified()) {
                accountActiveToken.setEmailIsVerified(true);
                accountActiveToken.getUser().setEnabled(true);
                accountActiveTokenRepository.save(accountActiveToken);
                model.addAttribute("emailVerified", true);
            } else {
                if (!accountActiveToken.getUser().isEnabled()) {
                    model.addAttribute("accountDisabled", true);
                } else {
                    model.addAttribute("emailVerified", false);
                }
            }
        } else {
            model.addAttribute("invalidToken", true);
        }
        System.out.println("Token = " + token);
        return "verified_status";
    }

    public static String getRandomTokenString() {
        String CHARS = "AbCdEfGhIjKlMnOpQrStWxYz_1234567890";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        int max = 900;
        int min = 600;
        while (sb.length() < (int)(Math.random() * ((max - min) + 1)) + min) {
            int index = (int) (rnd.nextFloat() * CHARS.length());
            sb.append(CHARS.charAt(index));
        }
        return sb.toString();
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
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);
        return redirectView;
    }

    @GetMapping("/test-payment-success")
    public String payTestSuccess() {
        return "dummy";
    }




}
