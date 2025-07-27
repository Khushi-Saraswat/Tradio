package com.trading.demo.service;

import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.trading.demo.domain.PaymentMethod;
import com.trading.demo.domain.PaymentOrderStatus;
import com.trading.demo.model.PaymentOrder;
import com.trading.demo.model.User;
import com.trading.demo.repository.PaymentOrderRepository;
import com.trading.demo.response.PaymentResponse;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${razorpay.key-id}")
    private String apiKey;

    @Value("${razorpay.key-secret}")
    private String apiSecret;

    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

    @Override
    public PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod) {
        PaymentOrder order = new PaymentOrder();
        order.setUser(user);
        order.setAmount(amount);
        order.setPaymentMethod(paymentMethod);

        return paymentOrderRepository.save(order);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) throws Exception {
        Optional<PaymentOrder> optionalPaymentOrder = paymentOrderRepository.findById(id);
        if (optionalPaymentOrder.isEmpty()) {
            throw new Exception("payment order not found with id " + id);
        }
        return optionalPaymentOrder.get();
    }

    @Override
    public Boolean ProccedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException {
        if (paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {

            if (paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)) {
                RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
                Payment payment = razorpay.payments.fetch(paymentId);

                Integer amount = payment.get("amount");
                String status = payment.get("status");
                if (status.equals("captured")) {
                    paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);

                    return true;
                }
                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                paymentOrderRepository.save(paymentOrder);
                return false;
            }
            paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
            paymentOrderRepository.save(paymentOrder);
            paymentOrderRepository.save(paymentOrder);
            return true;
        }

        return false;
    }

    @Override
    public PaymentResponse createRazorpayPaymentLink(User user,
            Long Amount,
            Long orderId)
            throws RazorpayException {

        Long amount = Amount * 100;

        try {
            // Instantiate a Razorpay client with your key ID and secret
            RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);

            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", amount);
            paymentLinkRequest.put("currency", "INR");

            // Create a JSON object with the customer details
            JSONObject customer = new JSONObject();
            customer.put("name", user.getFullName());

            customer.put("email", user.getEmail());
            paymentLinkRequest.put("customer", customer);

            // Create a JSON object with the notification settings
            JSONObject notify = new JSONObject();
            notify.put("email", true);
            paymentLinkRequest.put("notify", notify);

            // Set the reminder settings
            paymentLinkRequest.put("reminder_enable", true);

            // Set the callback URL and method
            paymentLinkRequest.put("callback_url", "http://localhost:5173/wallet/" + orderId);
            paymentLinkRequest.put("callback_method", "get");

            // Create the payment link using the paymentLink.create() method
            PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);

            String paymentLinkId = payment.get("id");
            String paymentLinkUrl = payment.get("short_url");

            PaymentResponse res = new PaymentResponse();
            res.setPayment_url(paymentLinkUrl);
            System.out.println(res + "res");
            return res;

        } catch (RazorpayException e) {

            System.out.println("Error creating payment link: " + e.getMessage());
            throw new RazorpayException(e.getMessage());
        }
    }

}
