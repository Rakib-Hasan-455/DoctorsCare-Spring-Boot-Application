package com.example.doctorscarespringbootapplication.dao;

import com.example.doctorscarespringbootapplication.entity.AppointDoctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Time;
import java.util.List;

public interface AppointDoctorRepository extends JpaRepository<AppointDoctor, Integer> {

    String countAllByAppointmentDateAndPatientID(String todaysDate, String patientId);

    List<AppointDoctor> findAllByAppointmentDateAndPatientIDAndAppointmentTimeGreaterThanOrderByAppointmentTimeAsc(String appointmentDate, String patientID, Time currentTime);
    List<AppointDoctor> findAllByAppointmentDateAndPatientIDOrderByAppointmentTimeAsc(String appointmentDate, String patientID);

    List<AppointDoctor> findAllByAppointmentDateAndDoctorIDAndAppointmentTimeGreaterThanOrderByAppointmentTimeAsc(String appointmentDate, String doctorID, Time currentTime);
    List<AppointDoctor> findAllByAppointmentDateAndDoctorIDOrderByAppointmentTimeAsc(String appointmentDate, String doctorID);

    Page<AppointDoctor> findAllByOrderByIdDesc(Pageable pageable);
    @Query(value = "SELECT doctorId FROM appoint_doctor GROUP BY doctorId ORDER BY COUNT(doctorId) desc LIMIT 3", nativeQuery = true)
    List<String> findTop3DoctorsNativeQuery();

    AppointDoctor findById(int id);

    String countAllByAppointmentDateAndDoctorID(String todayDate, String s);

    String countAllByAppointmentDate(String todayDate);

    List<AppointDoctor> findAllByAppointmentDateOrderByAppointmentDateAsc(String todayDate);
}
