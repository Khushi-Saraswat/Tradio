package com.trading.demo.model;

import com.trading.demo.domain.VerificationType;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class ForgotPasswordToken {
    @Id
    private String id;

    @OneToOne
    private Users user;

    private String otp;

    private VerificationType verificationType;

    private String sendTo;
}
