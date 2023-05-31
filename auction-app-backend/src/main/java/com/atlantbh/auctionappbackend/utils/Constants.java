package com.atlantbh.auctionappbackend.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class Constants {

    public static final String COOKIE_NAME = "auction_app_token";

    public static final int COOKIE_MAX_AGE = 4 * 60 * 60;

    public static final int LOGOUT_COOKIE_MAX_AGE = 0;

    public static final String[] AUTH_SECURED_ENDPOINTS = {
            "/api/v1/bid/app-user",
            "/api/v1/bid/create-bid",
            "/api/v1/highest-bidder/",
            "/api/v1/products/items/app-user",
            "/api/v1/products/add-item",
            "/api/v1/app-user/by-email",
    };

    public static final String S3_KEY_PREFIX = "product-images/";

    public static final String PAYMENT_METHOD_ID = "paymentMethodId";

    public static final String BID_DATE = "bidDate";

    public static final String AMOUNT = "amount";

    public static final String CURRENCY = "currency";

    public static final String PRODUCT_ID = "productId";

    public static final String PROVIDER_GOOGLE = "google";

    public static final String PROVIDER_FACEBOOK = "facebook";

    public static final String EMAIL_VALUE = "email";

    public static final String FIRST_NAME_VALUE = "first_name";

    public static final String LAST_NAME_VALUE = "last_name";

    public static final String SUB_VALUE = "id";

    public static final String FACEBOOK_GRAPH_API_URL = "https://graph.facebook.com/me?fields=id,email,first_name,last_name&access_token=";

    public static final String SEARCH_TERM_VALIDATOR = "[^a-zA-Z0-9 ]";

    public static final String AUCTION_FINISHED_EXCHANGE = "auction_finished_exchange";

    public static final String AUCTION_FINISHED_QUEUE = "auction_finished_queue";

    public static final String AUCTION_FINISHED_ROUTING_KEY = "auction.finished.routing.key";

    public static final String OUTBID_EXCHANGE = "outbid_exchange";

    public static final String OUTBID_QUEUE = "outbid_queue";

    public static final String OUTBID_ROUTING_KEY = "outbid.routing.key";

}
