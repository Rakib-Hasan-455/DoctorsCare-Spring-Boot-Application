package com.example.doctorscarespringbootapplication.controller.patient;

import com.example.doctorscarespringbootapplication.configuration.commerz.TransactionResponseValidator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/patient")
public class AppointDoctorValidator {
    @PostMapping("/appoint-doctor/pay-success-validator")
    public String paymentSuccessNew(@RequestParam Map<String, String> requestMap) throws Exception {
        TransactionResponseValidator transactionResponseValidator = new TransactionResponseValidator();
        transactionResponseValidator.receiveSuccessResponse(requestMap);
        return "Everything is okay! ";
    }
}
