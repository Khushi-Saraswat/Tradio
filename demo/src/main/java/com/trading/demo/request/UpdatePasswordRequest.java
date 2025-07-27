package com.trading.demo.request;

import com.trading.demo.domain.VerificationType;

import lombok.Data;

@Data
public class UpdatePasswordRequest {
    private String sendTo;
    private VerificationType verificationType;
}
