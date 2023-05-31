package com.atlantbh.auctionappbackend.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AppUserProductsResponse {

    private Long id;

    private String productName;

    private float startPrice;

    private String images;

    private ZonedDateTime endDate;

    private int numberOfBids;

    private Float highestBid;

}
