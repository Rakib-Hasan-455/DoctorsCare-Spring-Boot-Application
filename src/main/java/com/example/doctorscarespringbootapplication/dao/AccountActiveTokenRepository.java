package com.example.doctorscarespringbootapplication.dao;

import com.example.doctorscarespringbootapplication.entity.AccountActiveToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountActiveTokenRepository extends JpaRepository<AccountActiveToken, Integer> {
    @Query(value = "SELECT token FROM Account_Active_token where token = :n", nativeQuery = true)
    String isTokenAlreadyInRepositoryNative(@Param("n") String token);

    AccountActiveToken getAccountActiveTokenByToken(String token);
}
