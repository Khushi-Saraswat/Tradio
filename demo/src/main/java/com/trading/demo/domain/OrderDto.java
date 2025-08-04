package com.trading.demo.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDto {

    private Long id;
    private OrderType orderType;
    private BigDecimal price;
    private LocalDateTime timestamp;
    private String status;
    private String userEmail;
    private String assetSymbol;

    public OrderDto(Long id, OrderType orderType, BigDecimal price, LocalDateTime timestamp, String status,
            String userEmail, String assetSymbol) {
        this.id = id;
        this.orderType = orderType;
        this.price = price;
        this.timestamp = timestamp;
        this.status = status;
        this.userEmail = userEmail;
        this.assetSymbol = assetSymbol;
    }

    public OrderDto() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getAssetSymbol() {
        return assetSymbol;
    }

    public void setAssetSymbol(String assetSymbol) {
        this.assetSymbol = assetSymbol;
    }

    @Override
    public String toString() {
        return "OrderDto [id=" + id + ", orderType=" + orderType + ", price=" + price + ", timestamp=" + timestamp
                + ", status=" + status + ", userEmail=" + userEmail + ", assetSymbol=" + assetSymbol + "]";
    }

}
