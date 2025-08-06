package com.trading.demo.service;

import com.trading.demo.exception.WalletException;
import com.trading.demo.model.Order;
import com.trading.demo.model.Users;
import com.trading.demo.model.Wallet;

public interface WalletService {

    Wallet getUserWallet(Users user) throws WalletException;

    public Wallet addBalanceToWallet(Wallet wallet, Long money) throws WalletException;

    public Wallet findWalletById(Long id) throws WalletException;

    public Wallet walletToWalletTransfer(Users sender, Wallet receiverWallet, Long amount) throws WalletException;

    public Wallet payOrderPayment(Order order, Users user) throws WalletException;

}
