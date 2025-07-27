package com.trading.demo.service;

import java.util.List;

import com.trading.demo.domain.OrderType;
import com.trading.demo.model.Coin;
import com.trading.demo.model.Order;
import com.trading.demo.model.OrderItem;
import com.trading.demo.model.User;

public interface OrderService {

    Order createOrder(User user, OrderItem orderItem, OrderType orderType);

    Order getOrderById(Long orderId);

    List<Order> getAllOrdersForUser(Long userId, String orderType, String assetSymbol);

    void cancelOrder(Long orderId);

    // Order buyAsset(CreateOrderRequest req, Long userId, String jwt) throws
    // Exception;

    Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception;

    // Order sellAsset(CreateOrderRequest req,Long userId,String jwt) throws
    // Exception;

}
