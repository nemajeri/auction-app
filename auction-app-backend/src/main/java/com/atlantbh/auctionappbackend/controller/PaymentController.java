package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.response.PaymentResponse;
import com.atlantbh.auctionappbackend.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
@AllArgsConstructor
public class PaymentController {

    @Autowired
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody Map<String, Object> paymentData) throws StripeException {
        String paymentMethodId = (String) paymentData.get("paymentMethodId");
        double amount = ((Number) paymentData.get("amount")).doubleValue();
        String currency = (String) paymentData.get("currency");

        PaymentIntent paymentIntent = paymentService.payForProduct(amount, currency, paymentMethodId);

        PaymentResponse paymentResponse = new PaymentResponse(
                paymentIntent.getId(),
                paymentIntent.getStatus(),
                paymentIntent.getAmount(),
                paymentIntent.getCurrency()
        );

        return ResponseEntity.ok(paymentResponse);
    }
}
