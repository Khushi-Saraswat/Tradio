package com.trading.demo.service;

import java.util.List;

import com.trading.demo.domain.WalletTransactionType;
import com.trading.demo.model.Wallet;
import com.trading.demo.model.WalletTransaction;

public interface WalletTransactionService {
    WalletTransaction createTransaction(Wallet wallet,
            WalletTransactionType type,
            String transferId,
            String purpose,
            Long amount);

    List<WalletTransaction> getTransactions(Wallet wallet, WalletTransactionType type);

}
