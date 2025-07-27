package com.trading.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trading.demo.domain.WalletTransactionType;
import com.trading.demo.model.Wallet;
import com.trading.demo.model.WalletTransaction;
import com.trading.demo.repository.WalletTransactionRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WalletTransactionServiceImpl implements WalletTransactionService {

    @Autowired
    private WalletTransactionRepository walletTransactionRepository;

    @Override
    public WalletTransaction createTransaction(Wallet wallet,
            WalletTransactionType type,
            String transferId,
            String purpose,
            Long amount) {
        WalletTransaction transaction = new WalletTransaction();

        // transaction.setWallet(wallet);
        transaction.setWallet(wallet);
        transaction.setDate(LocalDate.now());
        transaction.setType(type);
        transaction.setTransferId(transferId);
        transaction.setPurpose(purpose);
        transaction.setAmount(amount);

        return walletTransactionRepository.save(transaction);
    }

    @Override
    public List<WalletTransaction> getTransactions(Wallet wallet, WalletTransactionType type) {
        // return walletTransactionRepository.findByWalletOrderByDateDesc(wallet);
        return walletTransactionRepository.findByWalletOrderByDateDesc(wallet);
    }
}
