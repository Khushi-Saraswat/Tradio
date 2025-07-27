package com.trading.demo.service;

import com.trading.demo.domain.VerificationType;
import com.trading.demo.model.ForgotPasswordToken;
import com.trading.demo.model.User;

public interface ForgotPasswordService {

    ForgotPasswordToken createToken(User user, String id, String otp,
            VerificationType verificationType, String sendTo);

    ForgotPasswordToken findById(String id);

    ForgotPasswordToken findByUser(Long userId);

    void deleteToken(ForgotPasswordToken token);

    boolean verifyToken(ForgotPasswordToken token, String otp);
}
