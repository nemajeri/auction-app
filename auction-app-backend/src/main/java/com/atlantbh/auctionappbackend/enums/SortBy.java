package com.atlantbh.auctionappbackend.enums;

import lombok.AllArgsConstructor;



public enum SortBy {
    START_DATE("startDate"),
    END_DATE("endDate"),
    SOLD("sold"),
    ACTIVE("Active");


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

}
