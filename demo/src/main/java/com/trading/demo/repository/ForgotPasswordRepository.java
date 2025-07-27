package com.trading.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.demo.model.ForgotPasswordToken;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPasswordToken, String> {
    ForgotPasswordToken findByUserId(Long userId);
}
