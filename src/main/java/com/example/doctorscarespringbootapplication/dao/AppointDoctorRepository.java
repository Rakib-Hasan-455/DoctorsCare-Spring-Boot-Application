package com.example.doctorscarespringbootapplication.dao;

import com.example.doctorscarespringbootapplication.entity.AppointDoctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface AppointDoctorRepository extends JpaRepository<AppointDoctor, Integer> {

    String countAllByAppointmentDateAndPatientID(Date todaysDate, String patientId);

    List<AppointDoctor> findAllByAppointmentDateAndPatientIDAndAppointmentTimeGreaterThanOrderByAppointmentTimeAsc(Date appointmentDate, String patientID, Time currentTime);

    List<AppointDoctor> findAllByAppointmentDateAndPatientIDOrderByAppointmentTimeAsc(Date appointmentDate, String patientID);

    List<AppointDoctor> findAllByAppointmentDateAndDoctorIDAndAppointmentTimeGreaterThanOrderByAppointmentTimeAsc(Date appointmentDate, String doctorID, Time currentTime);

    List<AppointDoctor> findAllByAppointmentDateAndDoctorIDOrderByAppointmentTimeAsc(Date appointmentDate, String doctorID);

    Page<AppointDoctor> findAllByOrderByIdDesc(Pageable pageable);


    @Query(value = "SELECT doctorId FROM appoint_doctor GROUP BY doctorId ORDER BY COUNT(doctorId) desc LIMIT 3", nativeQuery = true)
    List<String> findTop3DoctorsNativeQuery();

    AppointDoctor findById(int id);

    String countAllByAppointmentDateAndDoctorID(Date todayDate, String s);

    String countAllByAppointmentDate(Date todayDate);

    @Query(value = "SELECT sum(doctor_fee) FROM appoint_doctor", nativeQuery = true)
    String sumAllAppointEarningNative();

    @Query(value = "SELECT sum(doctor_fee) FROM appoint_doctor where appointment_date = :n", nativeQuery = true)
    String sumTodaysEarning(@Param("n") Date todayDate);

    @Query(value = "SELECT sum(doctor_fee) FROM appoint_doctor where appointment_date >= :m and appointment_date <= :n", nativeQuery = true)
    String sumWeeklyEarningNative(@Param("m") Date fromDate, @Param("n") Date toDate);

    @Query(value = "SELECT sum(doctor_fee) FROM appoint_doctor where appointment_date >= :m and appointment_date <= :n", nativeQuery = true)
    String summonthlyEarningNative(@Param("m") Date fromDate, @Param("n") Date toDate);


    List<AppointDoctor> findAllByAppointmentDateOrderByAppointmentDateAsc(String todayDate);

    Page<AppointDoctor> findAllByAppointmentDateOrderByIdDesc(Date todayDate, Pageable pageable);

    @Query(value = "SELECT sum(doctor_fee) FROM appoint_doctor where MONTH(appointment_date) = :m and YEAR(appointment_date) <= YEAR(CURRENT_DATE)", nativeQuery = true)
    String sumEarnByMonthNative(@Param("m") int i);
}
