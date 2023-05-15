package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.enums.PaymentStatus;
import com.atlantbh.auctionappbackend.repository.ProductRepository;
import com.atlantbh.auctionappbackend.response.PaymentResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import org.slf4j.Logger;

@Service
@AllArgsConstructor
public class PaymentService {

    private final ProductRepository productRepository;

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    public PaymentResponse payForProduct(double amount, String currency, String paymentMethodId, Long productId) {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (amount * 100))
                .setCurrency(currency)
                .setPaymentMethod(paymentMethodId)
                .setConfirm(true)
                .build();

        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);

            if (paymentIntent.getStatus().equals(PaymentStatus.SUCCESS.toString())) {
                productRepository.findById(productId).ifPresent(product -> {
                    product.setSold(true);
                    productRepository.save(product);
                });

                return new PaymentResponse(paymentIntent.getId(), PaymentStatus.SUCCESS, paymentIntent.getAmount(), paymentIntent.getCurrency());
            }

        } catch (StripeException e) {
            log.error("Stripe payment error", e);
            return new PaymentResponse(null, PaymentStatus.ERROR, null, null);
        }

        return new PaymentResponse(null, PaymentStatus.FAILED, null, null);
    }
}

