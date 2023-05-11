package com.atlantbh.auctionappbackend.enums;

public enum SortBy {

    START_DATE("startDate"),
    END_DATE("endDate"),
    SOLD("sold"),
    ACTIVE("Active"),
    PRICE_LOW_TO_HIGH("startPrice"),
    PRICE_HIGH_TO_LOW("startPrice"),
    DEFAULT_SORTING("productName");


    private final String value;

    public String getSort() {
        return value;
    }

    SortBy(String value) {
        this.value = value;
    }

    public static SortBy fromValue(String value) {
        for (SortBy sortBy : SortBy.values()) {
            if (sortBy.value.equalsIgnoreCase(value)) {
                return sortBy;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + value);
    }

    public static SortBy fromName(String name) {
        try {
            return SortBy.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unexpected name: " + name);
        }
    }

}
