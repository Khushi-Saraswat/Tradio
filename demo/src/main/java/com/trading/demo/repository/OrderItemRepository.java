package com.trading.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.demo.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
