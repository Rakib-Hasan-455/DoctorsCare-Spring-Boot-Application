package com.example.doctorscarespringbootapplication.controller.admin;


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
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminMainController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointDoctorRepository appointDoctorRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

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

    @GetMapping("/prescription-logs")
    public String prescriptionLogs(Model model, Principal principal) {
        model.addAttribute("title", "Appointment Logs");
        List<Prescription> prescriptionList = prescriptionRepository
                .findAllByOrderByIdDesc();
        if (prescriptionList.size() == 0) {
            model.addAttribute("noPrescription", "true");
        }
        model.addAttribute("prescriptionList", prescriptionList);
        addCommonData(model, principal);
        return "admin/admin_prescription_logs";
    }

    @PostMapping("/view-single-prescription")
    public String viewSinglePrescription(@RequestParam String appointmentID, Model model, Principal principal) {
        model.addAttribute("title", "Viewing Prescription");
        model.addAttribute("appointmentID", appointmentID);
        AppointDoctor appointDoctor = appointDoctorRepository.findById(Integer.parseInt(appointmentID));
        User patientUser = userRepository.getUserByEmailNative(appointDoctor.getPatientID());
        User doctorUser = userRepository.findById(Integer.parseInt(appointDoctor.getDoctorID()));
        model.addAttribute("appointDoctor", appointDoctor);
        model.addAttribute("patientUser", patientUser);
        model.addAttribute("doctorUser", doctorUser);
        addCommonData(model, principal);
        return "admin/admin_view_single_prescription";
    }

    @GetMapping("/patients-list")
    public String patientsList(Model model, Principal principal) {
        model.addAttribute("title", "Patients List");
        List<User> patientList = userRepository.findByRoleOrderByIdDesc("ROLE_PATIENT");
        if (patientList.size() == 0) {
            model.addAttribute("noPatient", true);
        }
        model.addAttribute("patientList", patientList);
        addCommonData(model, principal);
        return "admin/admin_patient_list";
    }

    @GetMapping("/doctors-list")
    public String doctorsList(Model model, Principal principal) {
        model.addAttribute("title", "Doctors List");
        List<User> doctorList = userRepository.findByRoleOrderByIdDesc("ROLE_DOCTOR");
        if (doctorList.size() == 0) {
            model.addAttribute("noDoctor", true);
        }
        model.addAttribute("doctorList", doctorList);
        addCommonData(model, principal);
        return "admin/admin_doctor_list";
    }

    @GetMapping("/earnings")
    public String earnings(Model model, Principal principal) {
        model.addAttribute("title", "Earnings");
         addCommonData(model, principal);
        return "admin/admin_earnings";
    }

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userEmail = principal.getName();
        User user = this.userRepository.getUserByEmailNative(userEmail);
        model.addAttribute("user", user);
    }
}
