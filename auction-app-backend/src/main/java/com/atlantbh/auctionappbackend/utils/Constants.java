package com.atlantbh.auctionappbackend.utils;

public final class Constants {

    private Constants() {

    }

    public static final String SOCIAL_MEDIA_COOKIE_NAME = "auction_app_social_media_token";
    public static final String LOGOUT_COOKIE_NAME = "auction_app_logout_token";
    public static final String LOGIN_COOKIE_NAME = "auction_app_token";

    public static final int SOCIAL_MEDIA_COOKIE_MAX_AGE = 4 * 60 * 60;
    public static final int LOGOUT_COOKIE_MAX_AGE = 0;
}
