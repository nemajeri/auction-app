package com.atlantbh.auctionappbackend.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {
    private String errorCode;
    private String message;

}
