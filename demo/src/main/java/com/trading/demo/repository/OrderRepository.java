package com.trading.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.demo.model.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    public List<Order> findByUserId(Long userId);
}
