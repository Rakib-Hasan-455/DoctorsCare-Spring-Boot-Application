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
import org.springframework.context.annotation.ComponentScan;
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
    @PostMapping("/pay-success-validator")
    public String paymentSuccessNew(@RequestParam Map<String, String> requestMap) throws Exception {
        TransactionResponseValidator transactionResponseValidator = new TransactionResponseValidator();
        transactionResponseValidator.receiveSuccessResponse(requestMap);
        System.out.println("Pay-success-new is called");
        return "Everything is okay! ";
    }
}
