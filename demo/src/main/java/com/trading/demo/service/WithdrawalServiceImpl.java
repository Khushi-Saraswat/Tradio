package com.trading.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trading.demo.domain.WithdrawalStatus;
import com.trading.demo.model.Users;
import com.trading.demo.model.Withdrawal;
import com.trading.demo.repository.WithdrawalRepository;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {
    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Override
    public Withdrawal requestWithdrawal(Long amount, Users user) {
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setAmount(amount);
        // withdrawal.setStatus(WithdrawalStatus.PENDING);
        withdrawal.setWithdrawalStatus(WithdrawalStatus.PENDING);
        withdrawal.setDate(LocalDateTime.now());
        withdrawal.setUser(user);
        return withdrawalRepository.save(withdrawal);
    }

    @Override
    public Withdrawal procedWithdrawal(Long withdrawalId, boolean accept) throws Exception {
        Optional<Withdrawal> withdrawalOptional = withdrawalRepository.findById(withdrawalId);

        if (withdrawalOptional.isEmpty()) {
            throw new Exception("withdrawal id is wrong...");
        }

        Withdrawal withdrawal = withdrawalOptional.get();

        withdrawal.setDate(LocalDateTime.now());

        if (accept) {
            withdrawal.setWithdrawalStatus(WithdrawalStatus.SUCCESS);
        } else {
            withdrawal.setWithdrawalStatus(WithdrawalStatus.DECLINE);
        }

        return withdrawalRepository.save(withdrawal);
    }

    @Override
    public List<Withdrawal> getUsersWithdrawalHistory(Users user) {
        return withdrawalRepository.findByUserId(user.getId());
    }

    @Override
    public List<Withdrawal> getAllWithdrawalRequest() {
        return withdrawalRepository.findAll();
    }
}
