package com.trading.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

import com.trading.demo.domain.WithdrawalStatus;

@Entity
@Data
public class Withdrawal {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private WithdrawalStatus withdrawalStatus;

    private Long amount;

    @ManyToOne
    private User user;

    private LocalDateTime date;
}
