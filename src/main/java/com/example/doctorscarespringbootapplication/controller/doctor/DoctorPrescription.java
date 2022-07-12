package com.example.doctorscarespringbootapplication.controller.doctor;

import com.example.doctorscarespringbootapplication.dao.AppointDoctorRepository;
import com.example.doctorscarespringbootapplication.dao.UserRepository;
import com.example.doctorscarespringbootapplication.dto.DoctorGivePrescriptionDTO;
import com.example.doctorscarespringbootapplication.entity.AppointDoctor;
import com.example.doctorscarespringbootapplication.entity.Prescription;
import com.example.doctorscarespringbootapplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/doctor")
public class DoctorPrescription {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointDoctorRepository appointDoctorRepository;

    @PostMapping("/give-prescription")
    public String givePrescription(@RequestParam String appointmentID, Model model, Principal principal) {
        model.addAttribute("title", "Give Prescription");
        model.addAttribute("appointmentID", appointmentID);
        System.out.println("Give prescription appointid = " + appointmentID);
        if (!appointmentID.equals("")) {
            AppointDoctor appointDoctor = appointDoctorRepository.getById(Integer.parseInt(appointmentID));
            User patientUser = userRepository.getUserByEmailNative(appointDoctor.getPatientID());
            model.addAttribute("patientUser", patientUser);
            DoctorGivePrescriptionDTO doctorGivePrescriptionDTO = new DoctorGivePrescriptionDTO();
            if (appointDoctor.getPrescription() != null) {
                doctorGivePrescriptionDTO.setSymptoms(appointDoctor.getPrescription().getSymptoms());
                doctorGivePrescriptionDTO.setTests(appointDoctor.getPrescription().getTests());
                doctorGivePrescriptionDTO.setAdvice(appointDoctor.getPrescription().getAdvice());
                doctorGivePrescriptionDTO.setMedicines(appointDoctor.getPrescription().getMedicines());
            }
            model.addAttribute("doctorGivePrescriptionDTO", doctorGivePrescriptionDTO);
        } else {
            // If you don't have current appointment then you can't give prescription
            model.addAttribute("noAppointment", "true");
        }
        addCommonData(model, principal);
        return "doctor/doctor_give_prescription";
    }

    @PostMapping("/save-prescription")
    public String savePrescription(@ModelAttribute DoctorGivePrescriptionDTO doctorGivePrescriptionDTO, Model model, Principal principal) {
        model.addAttribute("title", "Save Prescription");
        model.addAttribute("appointmentID", doctorGivePrescriptionDTO.getAppointmentID());
        model.addAttribute("doctorGivePrescriptionDTO", doctorGivePrescriptionDTO);
        if (!doctorGivePrescriptionDTO.getAppointmentID().equals("")) {
            AppointDoctor appointDoctor = appointDoctorRepository.getById(Integer.parseInt(doctorGivePrescriptionDTO.getAppointmentID()));
            User patientUser = userRepository.getUserByEmailNative(appointDoctor.getPatientID());
            Prescription prescription = new Prescription(doctorGivePrescriptionDTO.getSymptoms(), doctorGivePrescriptionDTO.getTests(), doctorGivePrescriptionDTO.getAdvice(), doctorGivePrescriptionDTO.getMedicines());
            appointDoctor.setPrescription(prescription);
            prescription.setAppointDoctor(appointDoctor);
            appointDoctorRepository.save(appointDoctor);
            model.addAttribute("patientUser", patientUser);
        } else {
            // If you don't have current appointment then you can't give prescription
             model.addAttribute("noAppointment", "true");
        }
        model.addAttribute("prescriptionSaved", "true");
        addCommonData(model, principal);
        return "doctor/doctor_give_prescription";
    }

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userEmail = principal.getName();
        System.out.println("Email = "+userEmail);
        User user = this.userRepository.getUserByEmailNative(userEmail);
        model.addAttribute("user", user);
    }

}
