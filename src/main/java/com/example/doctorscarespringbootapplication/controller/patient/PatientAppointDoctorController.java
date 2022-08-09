package com.example.doctorscarespringbootapplication.controller.patient;

import com.example.doctorscarespringbootapplication.configuration.commerz.SSLCommerz;
import com.example.doctorscarespringbootapplication.configuration.commerz.Utility.ParameterBuilder;
import com.example.doctorscarespringbootapplication.dao.AdditionalDoctorsRepository;
import com.example.doctorscarespringbootapplication.dao.AppointDoctorRepository;
import com.example.doctorscarespringbootapplication.dao.AppointDoctorTransactionRepository;
import com.example.doctorscarespringbootapplication.dao.UserRepository;
import com.example.doctorscarespringbootapplication.dto.AppointDoctorDTO;
import com.example.doctorscarespringbootapplication.entity.AppointDoctor;
import com.example.doctorscarespringbootapplication.entity.AppointDoctorTransaction;
import com.example.doctorscarespringbootapplication.entity.Prescription;
import com.example.doctorscarespringbootapplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
@ComponentScan
@RequestMapping("/patient")
public class PatientAppointDoctorController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdditionalDoctorsRepository additionalDoctorsRepository;

    @Autowired
    private AppointDoctorRepository appointDoctorRepository;

    @Autowired
    private AppointDoctorTransactionRepository appointDoctorTransactionRepository;

    @GetMapping("/doctors-list/{doctortype}")
    public String patientDoctorsList(@PathVariable String doctortype, Model model, Principal principal) {
        model.addAttribute("title", "Patient Dashboard");
        addCommonData(model, principal);
        List<User> userList = this.userRepository.findByDoctorsAdditionalInfoDoctortype(doctortype);
        model.addAttribute("userList", userList);
        model.addAttribute("doctorType", doctortype);
        return "patient/patient_doctors_list";
    }

    @GetMapping("/appoint-doctor/{doctorID}")
    public String patientAppointDoctor(@PathVariable int doctorID, Model model, Principal principal) {
        model.addAttribute("title", "Appoint Doctor");
        addCommonData(model, principal);
        User userList = this.userRepository.findById(doctorID);
        updateAvailableTime(model, userList);
        model.addAttribute("userList", userList);
        return "patient/patient_appoint_doctor";
    }

    public static String getRandomTxIdString() {
        String CHARS = "AbCdEfGhIjKlMnOpQrStWxYz1234567890";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        int max = 10;
        int min = 5;
        while (sb.length() < (int)(Math.random() * ((max - min) + 1)) + min) {
            int index = (int) (rnd.nextFloat() * CHARS.length());
            sb.append(CHARS.charAt(index));
        }
        return sb.toString();
    }

    @PostMapping("/appoint-doctor/attempt-payment")
    public RedirectView patientAppointDoctorPaymentAttempt(@ModelAttribute AppointDoctorDTO appointDoctorDTO, Principal principal, Model model) throws Exception {
        String appointDrTxId = getRandomTxIdString() + userRepository.nextHibernateSequenceNative();
        Time appointTime = appointDoctorDTO.getAppointmentTime();
        String doctorID = appointDoctorDTO.getDoctorID(); // Doctor_ID needed
        String doctorFee = appointDoctorDTO.getDoctorFee(); // Doctor_Fee needed
        AppointDoctorTransaction appointDoctorTransaction = new AppointDoctorTransaction(appointDrTxId, doctorID, doctorFee, appointTime, "attempted");
        appointDoctorTransactionRepository.save(appointDoctorTransaction);

        User patientUser = userRepository.getUserByEmailNative(principal.getName());
        String baseurl = "https://doctors-care-application.herokuapp.com/";
//        SSL redirect to payment
        Map<String, String> transactionMap = ParameterBuilder.constructRequestParam(baseurl, doctorFee, appointDrTxId, patientUser.getName());
        SSLCommerz sslCommerz = new SSLCommerz("docto62f28257d4314", "docto62f28257d4314@ssl", true);
        String url = sslCommerz.initiateTransaction(transactionMap, false);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);
        return redirectView;
    }


    @PostMapping("/appoint-doctor/success")
    public String patientAppointDoctorSuccess(@RequestParam Map<String, String> requestMap, Model model, Principal principal) {
        model.addAttribute("title", "Appoint Doctor Successful");
        String patientID = principal.getName(); // Patient_ID needed
        String transactionId = requestMap.get("tran_id");
        AppointDoctorTransaction appointDoctorTransaction = appointDoctorTransactionRepository.findByTxid(transactionId); // Fetching from database
        String doctorID = appointDoctorTransaction.getDoctorId(); // Doctor_ID needed
        String doctorFee = appointDoctorTransaction.getDoctorFee(); // Doctor_Fee needed
        Time appointTime = appointDoctorTransaction.getAppointmentTime(); // Appoint_Time needed

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime localDateTime = LocalDateTime.now();

        Date appointDate = Date.valueOf(localDateTime.format(dateTimeFormatter)); // Appoint Date

        updateDoctorsAvailableTimeDB(doctorID, appointTime);

        AppointDoctor appointDoctor = new AppointDoctor(patientID, doctorID, doctorFee, appointTime, appointDate);
        appointDoctor = appointDoctorRepository.save(appointDoctor);
        Prescription prescription = new Prescription();
        prescription.setId(appointDoctor.getId());
        appointDoctor.setPrescription(prescription);
        prescription.setAppointDoctor(appointDoctor);
        model.addAttribute("appointDoctor", appointDoctor);
        appointDoctorRepository.save(appointDoctor);
        return "patient/patient_appoint_doctor_success";
    }

    private void updateDoctorsAvailableTimeDB(String doctorID, Time appointTime) {
        User userList = this.userRepository.findById(Integer.parseInt(doctorID));
        if (appointTime.toString().equals("10:00:00")) {
            userList.getDoctorsSchedule().set_10_00(false);
        }
        if (appointTime.toString().equals("10:30:00")) {
            userList.getDoctorsSchedule().set_10_30(false);
        }
        if (appointTime.toString().equals("11:00:00")) {
            userList.getDoctorsSchedule().set_11_00(false);
        }
        if (appointTime.toString().equals("11:30:00")) {
            userList.getDoctorsSchedule().set_11_30(false);
        }
        if (appointTime.toString().equals("12:00:00")) {
            userList.getDoctorsSchedule().set_12_00(false);
        }
        if (appointTime.toString().equals("14:00:00")) {
            userList.getDoctorsSchedule().set_2_00(false);
        }
        if (appointTime.toString().equals("14:30:00")) {
            userList.getDoctorsSchedule().set_2_30(false);
        }
        if (appointTime.toString().equals("15:00:00")) {
            userList.getDoctorsSchedule().set_3_00(false);
        }
        if (appointTime.toString().equals("15:30:00")) {
            userList.getDoctorsSchedule().set_3_30(false);
        }
        userRepository.save(userList);

    }

    private void updateAvailableTime(Model model, User userList) {
        DateTimeFormatter dtfHour = DateTimeFormatter.ofPattern("HH");
        LocalDateTime now = LocalDateTime.now();
        int hour = Integer.parseInt(dtfHour.format(now));
        model.addAttribute("currentHour", hour);
        if (hour >= 9) {
            userList.getDoctorsSchedule().set_10_00(false);
        }
        if (hour >= 10) {
            userList.getDoctorsSchedule().set_10_30(false);
            userList.getDoctorsSchedule().set_11_00(false);
        }

        if (hour >= 11) {
            userList.getDoctorsSchedule().set_11_30(false);
            userList.getDoctorsSchedule().set_12_00(false);
        }

        if (hour >= 13) {
            userList.getDoctorsSchedule().set_2_00(false);
        }

        if (hour >= 14) {
            userList.getDoctorsSchedule().set_2_30(false);
            userList.getDoctorsSchedule().set_3_00(false);
        }
        if (hour >= 15) {
            userList.getDoctorsSchedule().set_3_30(false);
        }

    }


    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userEmail = principal.getName();
        User user = this.userRepository.getUserByEmailNative(userEmail);
        model.addAttribute("user", user);
    }
}
