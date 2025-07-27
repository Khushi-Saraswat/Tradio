package com.trading.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.demo.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    public Wallet findByUserId(Long userId);

}
