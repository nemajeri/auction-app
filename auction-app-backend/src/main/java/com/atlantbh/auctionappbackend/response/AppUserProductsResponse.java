package com.atlantbh.auctionappbackend.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime endDate;

    private int numberOfBids;

    private Float highestBid;

}
