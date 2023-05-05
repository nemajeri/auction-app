package com.atlantbh.auctionappbackend.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product with Id " + id + " not found.");
    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}
