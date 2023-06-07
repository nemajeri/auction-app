package com.atlantbh.auctionappbackend.exception;

public class UnprocessableCSVFileException extends RuntimeException {
    public UnprocessableCSVFileException(String message) {
        super(message);
    }
}
