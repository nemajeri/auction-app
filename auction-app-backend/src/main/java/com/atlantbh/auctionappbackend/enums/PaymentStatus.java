package com.atlantbh.auctionappbackend.enums;

public enum PaymentStatus {
    SUCCESS("succeeded"),
    ERROR("error"),
    FAILED("failed");

    private final String status;

    PaymentStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
