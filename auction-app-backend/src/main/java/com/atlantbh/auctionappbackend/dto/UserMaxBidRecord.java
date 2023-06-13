package com.atlantbh.auctionappbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMaxBidRecord {

    private Long userId;

    private Float price;

}
