package com.example.doctorscarespringbootapplication.controller.doctor;


import com.example.doctorscarespringbootapplication.controller.patient.PatientMainController;
import com.example.doctorscarespringbootapplication.dao.AppointDoctorRepository;
import com.example.doctorscarespringbootapplication.dao.PrescriptionRepository;
import com.example.doctorscarespringbootapplication.dao.UserRepository;
import com.example.doctorscarespringbootapplication.entity.AppointDoctor;
import com.example.doctorscarespringbootapplication.entity.Prescription;
import com.example.doctorscarespringbootapplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Time;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/doctor")
public class DoctorMainController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppointDoctorRepository appointDoctorRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @GetMapping("/index")
    public String doctorHome(Model model, Principal principal) {
        model.addAttribute("title", "Doctors Dashboard");
        addCommonData(model, principal);
        return "doctor/doctor_home";
    }

    @GetMapping("/meet-patient")
    public String doctorMeetPatient(Model model, Principal principal) throws ParseException {
        model.addAttribute("title", "Meet Patient");

        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        String todaysDate = dtfDate.format(now);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime value = localDateTime.minus(30, ChronoUnit.MINUTES);
        Time currentTimeMinus30 = Time.valueOf(dateTimeFormatter.format(value));

        User userDoctor = this.userRepository.getUserByEmailNative(principal.getName());
        String doctorsID = userDoctor.getId()+"";

        List<AppointDoctor> appointDoctorList = this.appointDoctorRepository.findAllByAppointmentDateAndDoctorIDAndAppointmentTimeGreaterThanOrderByAppointmentTimeAsc(todaysDate, doctorsID, currentTimeMinus30);
        if (appointDoctorList.size() != 0) {
            AppointDoctor appointDoctor = appointDoctorList.get(0);
            System.out.println("Meet Patient Email: "+appointDoctor.getPatientID());

            model.addAttribute("senderEmailDoctor", principal.getName());
            model.addAttribute("receiverEmailDoctor", appointDoctor.getPatientID());
            model.addAttribute("appointmentID", appointDoctor.getId());

            User userPatient = this.userRepository.getUserByEmailNative(appointDoctor.getPatientID());
            model.addAttribute("patientName", userPatient.getName());

            PatientMainController.appointmentCountDown(model, dateTimeFormatter, localDateTime, appointDoctor);
        } else {
            model.addAttribute("noDoctorAppointment", "true");
        }

        addCommonData(model, principal);
        return "doctor/doctor_meet_patient";
    }

    @GetMapping("/todays-appointment")
    public String patientTodayAppointment(Model model, Principal principal) {
        model.addAttribute("title", "Today's Appointment");

        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        String todayDate = dtfDate.format(now);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime value = localDateTime.minus(30, ChronoUnit.MINUTES);
        Time currentTimeMinus30 = Time.valueOf(dateTimeFormatter.format(value));
        User user = userRepository.getUserByEmailNative(principal.getName());
        List<AppointDoctor> appointDoctorList = appointDoctorRepository
                .findAllByAppointmentDateAndDoctorIDAndAppointmentTimeGreaterThanOrderByAppointmentTimeAsc
                        (todayDate, user.getId()+"", currentTimeMinus30);


        if (appointDoctorList.size() != 0) {
            model.addAttribute("appointDoctorList", appointDoctorList);
        } else {
            model.addAttribute("noDoctorAppointment", "true");
        }
        addCommonData(model, principal);
        return "doctor/doctor_today's_appointment";
    }

    @GetMapping("/view-prescriptions")
    public String viewPrescriptions(Model model, Principal principal) {
        model.addAttribute("title", "View Prescriptions");
        User user = this.userRepository.getUserByEmailNative(principal.getName());
        List<Prescription> prescriptionList = prescriptionRepository
                .findByAppointDoctorDoctorIDOrderByIdDesc(user.getId()+"");
        if (prescriptionList.size() == 0) {
            model.addAttribute("noPrescription", "true");
        }
        model.addAttribute("prescriptionList", prescriptionList);
        addCommonData(model, principal);
        return "doctor/doctor_view_prescriptions";
    }

    @PostMapping("/view-single-prescription")
    public String viewSinglePrescription(@RequestParam String appointmentID, Model model, Principal principal) {
        model.addAttribute("title", "Single Prescription");
        model.addAttribute("appointmentID", appointmentID);
        System.out.println(appointmentID+ " appointment ID came...");
        AppointDoctor appointDoctor = appointDoctorRepository.findById(Integer.parseInt(appointmentID));
        User patientUser = userRepository.getUserByEmailNative(appointDoctor.getPatientID());
        model.addAttribute("appointDoctor", appointDoctor);
        model.addAttribute("patientUser", patientUser);
        addCommonData(model, principal);
        return "doctor/doctor_view_single_prescription";
    }


    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userEmail = principal.getName();
        System.out.println("Email = "+userEmail);
        User user = this.userRepository.getUserByEmailNative(userEmail);
        model.addAttribute("user", user);
    }
}
