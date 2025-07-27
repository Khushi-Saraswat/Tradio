package com.trading.demo.service;

import com.razorpay.RazorpayException;
import com.trading.demo.domain.PaymentMethod;
import com.trading.demo.model.PaymentOrder;
import com.trading.demo.model.User;
import com.trading.demo.response.PaymentResponse;

public interface PaymentService {

    PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod);

    PaymentOrder getPaymentOrderById(Long id) throws Exception;

    Boolean ProccedPaymentOrder(PaymentOrder paymentOrder,
            String paymentId) throws RazorpayException;

    PaymentResponse createRazorpayPaymentLink(User user,
            Long Amount,
            Long orderId) throws RazorpayException;

}
