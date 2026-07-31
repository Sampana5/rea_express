package com.rea.express.dao;

import com.rea.express.POJO.PasswordResetCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetCodeDao extends JpaRepository<PasswordResetCode, Integer> {

    Optional<PasswordResetCode> findTopByEmailAndUsedFalseOrderByIdDesc(String email);
}
