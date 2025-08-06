package com.trading.demo.service;

import com.trading.demo.domain.VerificationType;
import com.trading.demo.model.ForgotPasswordToken;
import com.trading.demo.model.Users;

public interface ForgotPasswordService {

    ForgotPasswordToken createToken(Users user, String id, String otp,
            VerificationType verificationType, String sendTo);

    ForgotPasswordToken findById(String id);

    ForgotPasswordToken findByUser(Long userId);

    void deleteToken(ForgotPasswordToken token);

    boolean verifyToken(ForgotPasswordToken token, String otp);
}
