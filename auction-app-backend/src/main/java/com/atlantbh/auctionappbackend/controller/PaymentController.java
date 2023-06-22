package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.response.PaymentResponse;
import com.atlantbh.auctionappbackend.service.PaymentService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.atlantbh.auctionappbackend.utils.Constants.*;

@RestController
@RequestMapping("/api/v1/payments")
@AllArgsConstructor
@Api(value = "Payments", tags = "Payment Controller")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @ApiOperation(value = "Create a new payment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created a payment"),
            @ApiResponse(code = 400, message = "Invalid request or payment data"),
            @ApiResponse(code = 500, message = "Internal server error or payment processing error")
    })
    public ResponseEntity<PaymentResponse> createPayment(
            @ApiParam(value = "Payment data containing payment method id, amount, currency, and product id", required = true)
            @RequestBody Map<String, Object> paymentData){
        String paymentMethodId = (String) paymentData.get(PAYMENT_METHOD_ID);
        double amount = ((Number) paymentData.get(AMOUNT)).doubleValue();
        String currency = (String) paymentData.get(CURRENCY);
        Long productId = Long.parseLong(paymentData.get(PRODUCT_ID).toString());

        PaymentResponse paymentResponse = paymentService.payForProduct(amount, currency, paymentMethodId, productId);

        return ResponseEntity.ok(paymentResponse);
    }
}
