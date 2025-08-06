package com.trading.demo.service;

import java.util.List;

import com.trading.demo.model.Users;
import com.trading.demo.model.Withdrawal;

public interface WithdrawalService {

    Withdrawal requestWithdrawal(Long amount, Users user);

    Withdrawal procedWithdrawal(Long withdrawalId, boolean accept) throws Exception;

    List<Withdrawal> getUsersWithdrawalHistory(Users user);

    List<Withdrawal> getAllWithdrawalRequest();
}
