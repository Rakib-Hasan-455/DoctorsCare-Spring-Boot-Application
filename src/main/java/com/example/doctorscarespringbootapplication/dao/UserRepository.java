package com.example.doctorscarespringbootapplication.dao;

import com.example.doctorscarespringbootapplication.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT * FROM USER WHERE email = :n", nativeQuery = true)
    User getUserByEmailNative(@Param("n") String email);

    List<User> findByDoctorsAdditionalInfoDoctortype(String doctorType);

    List<User> findByRole(String role);

    List<User> findByRoleAndEnabled(String role, boolean isEnabled);


    @Query(value = "SELECT * FROM USER WHERE ROLE = :m or ROLE = :n", nativeQuery = true)
    List<User> findByRoleNative(@Param("m") String rolePatient, @Param("n") String roleDoctor);

    @Query(value = "SELECT * FROM USER WHERE ROLE = :m or ROLE = :n and enabled = true", nativeQuery = true)
    List<User> findByRoleAndEnabledNative(@Param("m") String rolePatient, @Param("n") String roleDoctor);

    List<User> findByRoleOrderByIdDesc(String role);

    Page<User> findByRoleOrderByIdDesc(String role, Pageable pageable);

    List<User> findByDoctorsAdditionalInfoUser_id(int userID);

    User findById(int doctorID);

    long countByRole(String role_patient);

    long countByRoleAndEnabled(String role_patient, Boolean enabled);

    @Query(value = "SELECT next_val FROM hibernate_sequence Limit 1", nativeQuery = true)
    long nextHibernateSequenceNative();

}
