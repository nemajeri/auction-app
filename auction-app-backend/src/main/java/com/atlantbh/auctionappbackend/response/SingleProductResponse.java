package com.atlantbh.auctionappbackend.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SingleProductResponse {

    private Long id;

    private String productName;

    private String description;

    private Float startPrice;

    private List<String> images;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime endDate;

    private int numberOfBids;

    private float highestBid;

    private Long userId;

    private boolean isOwner;

    private boolean sold;

    private boolean  isUserHighestBidder;

}
