package com.atlantbh.auctionappbackend.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMaxBidRequest {

    public Long id;

    public Float price;

}
