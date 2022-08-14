package com.example.doctorscarespringbootapplication.dao;

import com.example.doctorscarespringbootapplication.entity.DoctorsAdditionalInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdditionalDoctorsRepository extends JpaRepository<DoctorsAdditionalInfo, Integer> {
    List<DoctorsAdditionalInfo> findDoctorsAdditionalInfoByDoctortype(String doctorsType);

    DoctorsAdditionalInfo findDoctorsAdditionalInfoByUser_id(int userID);

    int countByDoctortype(String doctorType);

}
