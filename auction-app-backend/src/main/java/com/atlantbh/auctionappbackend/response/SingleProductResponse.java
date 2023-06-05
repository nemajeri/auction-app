package com.atlantbh.auctionappbackend.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SingleProductResponse {

    private Long id;

    private String productName;

    private String description;

    private Float startPrice;

    private List<String> images;

    private ZonedDateTime startDate;

    private ZonedDateTime endDate;

    private int numberOfBids;

    private float highestBid;

    private Long userId;

    private boolean isOwner;

    private boolean sold;

    private Float userHighestBid;

    public SingleProductResponse(Long id, String productName, String description, float startPrice, List<String> images, ZonedDateTime endDate, int numberOfBids, Float highestBid, boolean sold, Long userId) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.startPrice = startPrice;
        this.images = images;
        this.endDate = endDate;
        this.numberOfBids = numberOfBids;
        this.highestBid = highestBid;
        this.sold = sold;
        this.userId = userId;
    }
}
