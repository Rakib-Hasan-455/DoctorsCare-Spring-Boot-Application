package com.example.doctorscarespringbootapplication.dao;

import com.example.doctorscarespringbootapplication.entity.AppointDoctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Time;
import java.util.List;

public interface AppointDoctorRepository extends JpaRepository<AppointDoctor, Integer> {

    List<AppointDoctor> findAllByAppointmentDateAndPatientIDAndAppointmentTimeGreaterThanOrderByAppointmentTimeAsc(String appointmentDate, String patientID, Time currentTime);

    List<AppointDoctor> findAllByAppointmentDateAndDoctorIDAndAppointmentTimeGreaterThanOrderByAppointmentTimeAsc(String appointmentDate, String doctorID, Time currentTime);

    List<AppointDoctor> findAllByOrderByIdDesc();
    AppointDoctor findById(int id);
}
