package com.trading.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.demo.model.VerificationCode;

public interface VerificationRepository extends JpaRepository<VerificationCode, Long> {
    VerificationCode findByUserId(Long userId);
}
