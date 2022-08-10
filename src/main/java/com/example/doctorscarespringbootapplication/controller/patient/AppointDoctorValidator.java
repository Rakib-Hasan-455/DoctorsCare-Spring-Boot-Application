package com.example.doctorscarespringbootapplication.controller.patient;

import com.example.doctorscarespringbootapplication.configuration.commerz.TransactionResponseValidator;
import com.example.doctorscarespringbootapplication.dao.AdditionalDoctorsRepository;
import com.example.doctorscarespringbootapplication.dao.AppointDoctorRepository;
import com.example.doctorscarespringbootapplication.dao.AppointDoctorTransactionRepository;
import com.example.doctorscarespringbootapplication.dao.UserRepository;
import com.example.doctorscarespringbootapplication.entity.AppointDoctor;
import com.example.doctorscarespringbootapplication.entity.AppointDoctorTransaction;
import com.example.doctorscarespringbootapplication.entity.Prescription;
import com.example.doctorscarespringbootapplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
public class AppointDoctorValidator {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdditionalDoctorsRepository additionalDoctorsRepository;

    @Autowired
    private AppointDoctorRepository appointDoctorRepository;

    @Autowired
    private AppointDoctorTransactionRepository appointDoctorTransactionRepository;
    @PostMapping("/appoint-doctor/success")
    public String patientAppointDoctorSuccess(@RequestParam Map<String, String> requestMap, Model model, Principal principal) {
        model.addAttribute("title", "Appoint Doctor Successful");
        String patientID = principal.getName(); // Patient_ID needed
        String transactionId = requestMap.get("tran_id");
        AppointDoctorTransaction appointDoctorTransaction = appointDoctorTransactionRepository.findByTxid(transactionId); // Fetching from database
        appointDoctorTransaction.setTransactionStatus("Paid");
        appointDoctorTransactionRepository.save(appointDoctorTransaction);
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


    @PostMapping("/pay-success-validator")
    public String paymentSuccessNew(@RequestParam Map<String, String> requestMap) throws Exception {
        TransactionResponseValidator transactionResponseValidator = new TransactionResponseValidator();
        transactionResponseValidator.receiveSuccessResponse(requestMap);
        System.out.println("Pay-success-new is called");
        return "Everything is okay! ";
    }
}
