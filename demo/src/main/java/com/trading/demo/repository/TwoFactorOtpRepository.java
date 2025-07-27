package com.trading.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.demo.model.TwoFactorOTP;

public interface TwoFactorOtpRepository extends JpaRepository<TwoFactorOTP, String> {

    TwoFactorOTP findByUserId(Long userId);
}
