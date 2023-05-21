package com.atlantbh.auctionappbackend.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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

    private boolean isOwner;

    private boolean sold;

    private Float userHighestBid;

}
