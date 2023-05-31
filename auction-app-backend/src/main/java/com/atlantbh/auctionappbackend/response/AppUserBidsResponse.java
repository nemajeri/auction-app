package com.atlantbh.auctionappbackend.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppUserBidsResponse {

    private Long id;

    private float price;

    private SingleProductResponse product;

    private Long userId;
}
