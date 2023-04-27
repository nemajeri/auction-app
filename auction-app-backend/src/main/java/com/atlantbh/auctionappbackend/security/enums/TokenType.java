package com.atlantbh.auctionappbackend.security.enums;

import java.time.temporal.ChronoUnit;

public enum TokenType {

    REMEMBER_ME(2, ChronoUnit.DAYS),
    STANDARD(4, ChronoUnit.HOURS);

    private final long duration;
    private final ChronoUnit unit;

    TokenType(long duration, ChronoUnit unit) {
        this.duration = duration;
        this.unit = unit;
    }

    public long getDuration() {
        return duration;
    }

    public ChronoUnit getUnit() {
        return unit;
    }
}

