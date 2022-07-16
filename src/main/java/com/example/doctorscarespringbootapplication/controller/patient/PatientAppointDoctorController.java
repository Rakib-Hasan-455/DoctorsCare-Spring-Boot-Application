package com.example.doctorscarespringbootapplication.controller.patient;

import com.example.doctorscarespringbootapplication.dao.AdditionalDoctorsRepository;
import com.example.doctorscarespringbootapplication.dao.AppointDoctorRepository;
import com.example.doctorscarespringbootapplication.dao.UserRepository;
import com.example.doctorscarespringbootapplication.dto.AppointDoctorDTO;
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
import java.util.List;

@Controller
@RequestMapping("/patient")
public class PatientAppointDoctorController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdditionalDoctorsRepository additionalDoctorsRepository;

    @Autowired
    private AppointDoctorRepository appointDoctorRepository;

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

    @PostMapping("/appoint-doctor/success")
    public String patientAppointDoctorSuccess(@ModelAttribute AppointDoctorDTO appointDoctorDTO, Principal principal, Model model) {
        model.addAttribute("title", "Appoint Doctor Successful");
        String patientID = principal.getName(); // Patient_ID
        String doctorID = appointDoctorDTO.getDoctorID(); // Doctor_ID
        String doctorFee = appointDoctorDTO.getDoctorFee(); // Doctor_Fee
        Time appointTime = appointDoctorDTO.getAppointmentTime(); // Appoint_Time

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime localDateTime = LocalDateTime.now();

        String appointDate = localDateTime.format(dateTimeFormatter); // Appoint Date

        updateDoctorsAvailableTimeDB(doctorID, appointTime);

        AppointDoctor appointDoctor = new AppointDoctor(patientID, doctorID, doctorFee, appointTime, appointDate);
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
        DateTimeFormatter dtfMinutes = DateTimeFormatter.ofPattern("mm");

        LocalDateTime now = LocalDateTime.now();
        int hour = Integer.parseInt(dtfHour.format(now));
        int minutes = Integer.parseInt(dtfMinutes.format(now));
        String time = hour + ":" + minutes;

        model.addAttribute("currentTime", time);
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
