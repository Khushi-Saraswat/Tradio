package com.trading.demo.service;

import com.trading.demo.domain.VerificationType;
import com.trading.demo.model.Users;
import com.trading.demo.model.VerificationCode;

public interface VerificationService {
    VerificationCode sendVerificationOTP(Users user, VerificationType verificationType);

    VerificationCode findVerificationById(Long id) throws Exception;

    VerificationCode findUsersVerification(Users user) throws Exception;

    Boolean VerifyOtp(String opt, VerificationCode verificationCode);

    void deleteVerification(VerificationCode verificationCode);
}
