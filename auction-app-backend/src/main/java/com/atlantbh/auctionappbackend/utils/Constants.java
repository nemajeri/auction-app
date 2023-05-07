package com.atlantbh.auctionappbackend.utils;

public final class Constants {

    private Constants() {

    }

    public static final String COOKIE_NAME = "auction_app_token";

    public static final int COOKIE_MAX_AGE = 4 * 60 * 60;

    public static final int LOGOUT_COOKIE_MAX_AGE = 0;

    public static final String[] AUTH_SECURED_ENDPOINTS = {
            "/api/v1/bid/app-user",
            "/api/v1/bid/create-bid",
            "/api/v1/products/items/app-user",
            "/api/v1/products/add-item",
    };

    public static final String S3_KEY_PREFIX = "product-images/";
}
