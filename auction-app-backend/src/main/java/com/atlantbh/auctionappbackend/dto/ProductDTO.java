package com.atlantbh.auctionappbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDTO {

    private Long id;

    private String productName;

    private String description;

    private Float startPrice;

    private List<String> images;

    private Date startDate;

    private Date endDate;

    private BigInteger numberOfBids;

    private BigInteger highestBid;
}
