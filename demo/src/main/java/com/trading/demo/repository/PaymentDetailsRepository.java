package com.trading.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.demo.model.PaymentDetails;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Long> {

    PaymentDetails getPaymentDetailsByUserId(Long userId);
}
