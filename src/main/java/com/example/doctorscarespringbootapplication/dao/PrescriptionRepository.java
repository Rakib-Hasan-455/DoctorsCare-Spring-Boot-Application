package com.example.doctorscarespringbootapplication.dao;

import com.example.doctorscarespringbootapplication.entity.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
    String countAllByAppointDoctorPatientID(String patientId);

    String countAllByAppointDoctorAppointmentDateAndMedicinesIsNotNullAndAppointDoctorPatientID(Date todaysDate, String patientId);

    List<Prescription> findByAppointDoctorPatientIDOrderByIdDesc(String patientIDEmail);

    Page<Prescription> findByAppointDoctorPatientIDAndSymptomsIsNotOrderByIdDesc(String patientIDEmail, Pageable pageable, String symptoms);

    List<Prescription> findByAppointDoctorDoctorIDOrderByIdDesc(String doctorID);

    Page<Prescription> findByAppointDoctorDoctorIDAndSymptomsIsNotOrderByIdDesc(String doctorID, Pageable pageable, String symptoms);

    List<Prescription> findAllByOrderByIdDesc();

    Page<Prescription> findAllBySymptomsIsNotOrderByIdDesc(Pageable pageable, String symptoms);

    String countAllByAppointDoctorAppointmentDateAndMedicinesIsNotNullAndAppointDoctorDoctorID(Date todayDate, String s);

    String countAllByAppointDoctorDoctorID(String s);

    String countAllByAppointDoctorAppointmentDateAndMedicinesIsNotNull(Date todayDate);
}

