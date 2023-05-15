package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.response.PaymentResponse;
import com.atlantbh.auctionappbackend.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.atlantbh.auctionappbackend.utils.Constants.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/api/v1/payments")
@AllArgsConstructor
public class PaymentController {

    @Autowired
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody Map<String, Object> paymentData){
            String paymentMethodId = (String) paymentData.get(PAYMENT_METHOD_ID);
            double amount = ((Number) paymentData.get(AMOUNT)).doubleValue();
            String currency = (String) paymentData.get(CURRENCY);
            Long productId = Long.parseLong(paymentData.get(PRODUCT_ID).toString());

            PaymentResponse paymentResponse = paymentService.payForProduct(amount, currency, paymentMethodId, productId);

            return ResponseEntity.ok(paymentResponse);
    }
}
