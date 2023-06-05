package com.atlantbh.auctionappbackend.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HighlightedProductResponse {

     private Long id;

     private String productName;

     private float startPrice;

     private String images;

     private String description;
}
