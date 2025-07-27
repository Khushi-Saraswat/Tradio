package com.trading.demo.service;

import com.trading.demo.exception.WalletException;
import com.trading.demo.model.Order;
import com.trading.demo.model.User;
import com.trading.demo.model.Wallet;

public interface WalletService {

    Wallet getUserWallet(User user) throws WalletException;

    public Wallet addBalanceToWallet(Wallet wallet, Long money) throws WalletException;

    public Wallet findWalletById(Long id) throws WalletException;

    public Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long amount) throws WalletException;

    public Wallet payOrderPayment(Order order, User user) throws WalletException;

}
