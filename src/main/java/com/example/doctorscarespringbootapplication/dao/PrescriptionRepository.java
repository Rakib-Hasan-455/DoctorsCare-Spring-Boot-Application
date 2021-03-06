package com.example.doctorscarespringbootapplication.dao;

import com.example.doctorscarespringbootapplication.entity.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
    List<Prescription> findByAppointDoctorPatientIDOrderByIdDesc(String patientIDEmail);
    Page<Prescription> findByAppointDoctorPatientIDAndSymptomsNotNullOrderByIdDesc(String patientIDEmail, Pageable pageable);

    List<Prescription> findByAppointDoctorDoctorIDOrderByIdDesc(String doctorID);
    Page<Prescription> findByAppointDoctorDoctorIDAndSymptomsNotNullOrderByIdDesc(String doctorID, Pageable pageable);

    List<Prescription> findAllByOrderByIdDesc();

    Page<Prescription> findAllByMedicinesNotNullOrderByIdDesc(Pageable pageable);
}

