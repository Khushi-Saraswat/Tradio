package com.trading.demo.request;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private String coinId;
    private double quantity;
    private com.trading.demo.domain.OrderType orderType;
}
