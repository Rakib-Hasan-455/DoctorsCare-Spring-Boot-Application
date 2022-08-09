package com.example.doctorscarespringbootapplication.dao;

import com.example.doctorscarespringbootapplication.entity.AppointDoctorTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointDoctorTransactionRepository extends JpaRepository<AppointDoctorTransaction, Long> {
    public AppointDoctorTransaction findByTxid(String txId);
}
