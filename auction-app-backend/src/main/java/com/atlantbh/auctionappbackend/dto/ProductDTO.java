package com.atlantbh.auctionappbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDTO {
    private Long id;
    private String productName;
    private String description;
    private Float startPrice;
    private String images;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigInteger numberOfBids;
    private BigDecimal highestBid;
    private Long categoryId;
    private String categoryName;

}
