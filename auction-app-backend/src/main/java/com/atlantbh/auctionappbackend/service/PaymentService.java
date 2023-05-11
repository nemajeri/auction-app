package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.model.Product;
import com.atlantbh.auctionappbackend.repository.ProductRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.atlantbh.auctionappbackend.utils.Constants.PAYMENT_SUCCESS;

@Service
@AllArgsConstructor
public class PaymentService {

    private final ProductRepository productRepository;

    public PaymentIntent payForProduct(double amount, String currency, String paymentMethodId, Long productId) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (amount * 100))
                .setCurrency(currency)
                .setPaymentMethod(paymentMethodId)
                .setConfirm(true)
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        if(paymentIntent.getStatus().equals(PAYMENT_SUCCESS)) {
            Optional<Product> optProduct = productRepository.findById(productId);
            if (optProduct.isPresent()) {
                Product product = optProduct.get();
                product.setSold(true);
                productRepository.save(product);
            }
        }

        return paymentIntent;
    }
}
