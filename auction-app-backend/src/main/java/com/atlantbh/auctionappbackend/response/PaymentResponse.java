package com.atlantbh.auctionappbackend.response;

import com.atlantbh.auctionappbackend.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PaymentResponse {
    private String id;
    private PaymentStatus status;
    private Long amount;
    private String currency;
}

