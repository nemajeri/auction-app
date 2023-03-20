package com.atlantbh.auctionappbackend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDTO {

    private Long id;

    private String productName;

    private String description;

    private Float startPrice;

    private String images;
}
