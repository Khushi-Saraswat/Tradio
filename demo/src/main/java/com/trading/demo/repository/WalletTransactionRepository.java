package com.trading.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.demo.model.Wallet;
import com.trading.demo.model.WalletTransaction;

import java.util.List;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {

    List<WalletTransaction> findByWalletOrderByDateDesc(Wallet wallet);

}
