package com.trading.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.demo.model.PaymentOrder;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
}
