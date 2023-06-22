package com.atlantbh.auctionappbackend.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuth2LoginRequest {

    private String provider;

    private String token;

}
