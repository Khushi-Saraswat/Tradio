package com.trading.demo.model;

import com.trading.demo.domain.VerificationType;

import lombok.Data;

@Data
public class TwoFactorAuth {

    private boolean isEnabled = false;
    private VerificationType sendTo;
}
