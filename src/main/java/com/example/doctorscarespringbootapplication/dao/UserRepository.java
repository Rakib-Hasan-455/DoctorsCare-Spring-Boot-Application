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

    List<User> findByRoleOrderByIdDesc(String role);

    Page<User> findByRoleOrderByIdDesc(String role, Pageable pageable);

    List<User> findByDoctorsAdditionalInfoUser_id(int userID);

    User findById(int doctorID);
}
