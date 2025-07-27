package com.trading.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.demo.model.Coin;

public interface CoinRepository extends JpaRepository<Coin, String> {
}
