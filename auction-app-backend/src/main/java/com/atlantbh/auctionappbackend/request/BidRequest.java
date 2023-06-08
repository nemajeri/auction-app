package com.atlantbh.auctionappbackend.request;


import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class BidRequest {

    private Long productId;

    @Min(value = 1, message = "Amount must be greater than 0.")
    private float amount;

    private Long userId;

}
