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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
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
        model.addAttribute("title", "Patient | Patient Signup");

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

        String token = getRandomTokenString()+userRepository.nextHibernateSequenceNative();
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
                "<h4>Dear Patient, <br> To verify your account please click this link.<h4> <h3><a target=\"_blank\" href=\"https://doctors-care-application.herokuapp.com/verify-account?token="+user.getAccountActiveToken().getToken()+"\"> Click Here To Active Your Account</a></h3>" +
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
        model.addAttribute("title", "Doctor | Doctor Signup");

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

        String token = getRandomTokenString()+userRepository.nextHibernateSequenceNative();
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
                "<h4>Dear Doctor, <br> To verify your account please click this link.<h4> <h3><a target=\"_blank\" href=\"https://doctors-care-application.herokuapp.com/verify-account?token="+user.getAccountActiveToken().getToken()+"\"> Click Here To Active Your Account</a></h3>" +
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
    public String login(Model model) {
        model.addAttribute("title", "Login to account");
        return "login";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        model.addAttribute("title", "Forgot Password");
        model.addAttribute("sendOtpDiv", true);
        model.addAttribute("verifyOtpDiv", false);
        model.addAttribute("changePasswordDiv", false);
        return "forgot_password";
    }

    @GetMapping("/send-otp")
    public String sendOTP(@RequestParam String email, Model model, HttpSession httpSession) throws MessagingException {
        model.addAttribute("title", "Forgot Password");
        User user = userRepository.getUserByEmailNative(email);
        if (user != null && !user.getRole().equals("ROLE_ADMIN")) {
            if (!user.isEnabled() && user.getAccountActiveToken().isEmailIsVerified()) {
                model.addAttribute("accountDisabled", true);
                model.addAttribute("sendOtpDiv", true);
                model.addAttribute("verifyOtpDiv", false);
                model.addAttribute("changePasswordDiv", false);
            } else if (!user.isEnabled() && !user.getAccountActiveToken().isEmailIsVerified()) {
                model.addAttribute("accountNotVerified", true);
                model.addAttribute("sendOtpDiv", true);
                model.addAttribute("verifyOtpDiv", false);
                model.addAttribute("changePasswordDiv", false);
            } else {
                model.addAttribute("title", "Enter Your OTP");
                model.addAttribute("sendOtpDiv", false);
                model.addAttribute("verifyOtpDiv", true);
                model.addAttribute("changePasswordDiv", false);
                String forgotPassOTP = String.valueOf((int) (Math.random() * (999999 - 999)) + 999);
                emailSenderServiceJava.sendEmail(email, "Doctors Care OTP Verification", "<h3>Dear user,<br>" +
                        "To change your password you need to use this OTP code. <br>" +
                        "Your OTP code is </h3> <h2>"+forgotPassOTP+"</h2> " +
                        "<h3>Regards,<br>" +
                        "Doctors Care Team.</h3>");
                httpSession.setAttribute("forgotPassOTP", forgotPassOTP);
                httpSession.setAttribute("forgotPassEmail", email);
            }
        } else {
            model.addAttribute("invalidEmail", true);
            model.addAttribute("sendOtpDiv", true);
            model.addAttribute("verifyOtpDiv", false);
            model.addAttribute("changePasswordDiv", false);
        }
        return "forgot_password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String newPassword, @RequestParam String confirmPassword, Model model, HttpSession httpSession) {
        model.addAttribute("title", "Change Your Password");
        System.out.println(newPassword);
        System.out.println(confirmPassword);
        if (newPassword.equals(confirmPassword)) {
            String passChangerEmail = (String) httpSession.getAttribute("forgotPassEmail");
            User user = userRepository.getUserByEmailNative(passChangerEmail);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            model.addAttribute("title", "Change Password Successful!");
            model.addAttribute("passwordChanged", true);
        } else {
            model.addAttribute("title", "Change Password Unsuccessful!");
            model.addAttribute("passwordChanged", false);
        }
        model.addAttribute("sendOtpDiv", false);
        model.addAttribute("verifyOtpDiv", false);
        model.addAttribute("changePasswordDiv", true);
        return "forgot_password";
    }

    @GetMapping("/verify-otp")
    public String verifyOTP(@RequestParam String OTP, Model model, HttpSession httpSession) {
        model.addAttribute("title", "Enter Your OTP");
        String generatedOTP = (String) httpSession.getAttribute("forgotPassOTP");
        if (generatedOTP.equals(OTP)) {
            model.addAttribute("title", "Change Your Password");
            model.addAttribute("sendOtpDiv", false);
            model.addAttribute("verifyOtpDiv", false);
            model.addAttribute("changePasswordDiv", true);
        } else {
            model.addAttribute("title", "OTP Doesn't Match. Enter Your OTP Again");
            model.addAttribute("OTPNotMatch", true);
            model.addAttribute("sendOtpDiv", false);
            model.addAttribute("verifyOtpDiv", true);
            model.addAttribute("changePasswordDiv", false);
        }
        return "forgot_password";
    }

    @GetMapping("/test-nextId")
    @ResponseBody
    public String payTest() throws Exception {
        return userRepository.nextHibernateSequenceNative()+" This is next id?";
    }
}
