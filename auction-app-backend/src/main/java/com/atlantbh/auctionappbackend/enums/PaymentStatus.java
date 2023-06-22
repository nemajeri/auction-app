package com.atlantbh.auctionappbackend.enums;

public enum PaymentStatus {
    SUCCESS("succeeded"),
    ERROR("error"),
    FAILED("failed");

    private final String status;

    PaymentStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

