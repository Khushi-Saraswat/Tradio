package com.trading.demo.service;

import com.trading.demo.model.TwoFactorOTP;
import com.trading.demo.model.Users;

public interface TwoFactorOtpService {

    TwoFactorOTP createTwoFactorOtp(Users user, String otp, String jwt);

    TwoFactorOTP findByUser(Long userId);

    TwoFactorOTP findById(String id);

    boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOtp, String otp);

    void deleteTwoFactorOtp(TwoFactorOTP twoFactorOTP);

}
