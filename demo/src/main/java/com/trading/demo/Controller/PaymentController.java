package com.trading.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.RazorpayException;
import com.trading.demo.domain.PaymentMethod;
import com.trading.demo.exception.UserException;
import com.trading.demo.model.PaymentOrder;
import com.trading.demo.model.Users;
import com.trading.demo.response.PaymentResponse;
import com.trading.demo.service.PaymentService;
import com.trading.demo.service.UserService;

@RestController
public class PaymentController {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/api/payment/{paymentMethod}/amount/{amount}")
    public ResponseEntity<PaymentResponse> paymentHandler(
            @PathVariable PaymentMethod paymentMethod,
            @PathVariable Long amount,
            @RequestHeader("Authorization") String jwt) throws UserException, RazorpayException {

        Users user = userService.findUserProfileByJwt(jwt);
        System.out.println("puser" + "" + user);
        PaymentResponse paymentResponse = null;

        PaymentOrder order = paymentService.createOrder(user, amount, paymentMethod);

        if (paymentMethod.equals(PaymentMethod.RAZORPAY)) {
            paymentResponse = paymentService.createRazorpayPaymentLink(user, amount,
                    order.getId());
        }
        System.out.println(paymentMethod + "payment method");
        System.out.println(paymentResponse + "payment Response");

        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }

}
