package com.trading.demo.service;

import com.trading.demo.model.PaymentDetails;
import com.trading.demo.model.User;

import jakarta.persistence.OneToOne;

public interface PaymentDetailsService {
    public PaymentDetails addPaymentDetails(String accountNumber,
            String accountHolderName,
            String ifsc,
            String bankName,
            User user);

    public PaymentDetails getUsersPaymentDetails(User user);

}
