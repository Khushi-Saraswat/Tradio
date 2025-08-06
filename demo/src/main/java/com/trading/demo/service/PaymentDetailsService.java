package com.trading.demo.service;

import com.trading.demo.model.PaymentDetails;
import com.trading.demo.model.Users;

public interface PaymentDetailsService {
    public PaymentDetails addPaymentDetails(String accountNumber,
            String accountHolderName,
            String ifsc,
            String bankName,
            Users user);

    public PaymentDetails getUsersPaymentDetails(Users user);

}
