package com.atlantbh.auctionappbackend.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMaxBidRequest {

    private Long userId;

    private Float price;

}
