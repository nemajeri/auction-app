package com.atlantbh.auctionappbackend.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class Constants {

    public final String COOKIE_NAME = "auction_app_token";

    public final int COOKIE_MAX_AGE = 4 * 60 * 60;

    public final int LOGOUT_COOKIE_MAX_AGE = 0;

    public final String[] AUTH_SECURED_ENDPOINTS = {
            "/api/v1/bid/app-user",
            "/api/v1/bid/create-bid",
            "/api/v1/highest-bidder/",
            "/api/v1/products/items/app-user",
            "/api/v1/products/add-item",
            "/api/v1/app-user/by-email",
            "/api/v1/auth/logout",
            "/api/v1/payments",
            "/api/v1/products/recommended",
            "/api/v1/products/upload-csv-file"
    };

    public final String S3_KEY_PREFIX = "product-images/";

    public final String PAYMENT_METHOD_ID = "paymentMethodId";

    public final String BID_DATE = "bidDate";

    public final String AMOUNT = "amount";

    public final String CURRENCY = "currency";

    public final String PRODUCT_ID = "productId";

    public final String PROVIDER_GOOGLE = "google";

    public final String PROVIDER_FACEBOOK = "facebook";

    public final String EMAIL_VALUE = "email";

    public final String FIRST_NAME_VALUE = "first_name";

    public final String LAST_NAME_VALUE = "last_name";

    public final String SUB_VALUE = "id";

    public final String FACEBOOK_GRAPH_API_URL = "https://graph.facebook.com/me?fields=id,email,first_name,last_name&access_token=";

    public final String SEARCH_VALIDATOR = "^[a-zA-Z0-9 ,.-]*$";

    public final String AUCTION_FINISHED_EXCHANGE = "auction_finished_exchange";

    public final String AUCTION_FINISHED_QUEUE = "auction_finished_queue";

    public final String AUCTION_FINISHED_ROUTING_KEY = "auction.finished.routing.key";

    public final String OUTBID_EXCHANGE = "outbid_exchange";

    public final String OUTBID_QUEUE = "outbid_queue";

    public final String OUTBID_ROUTING_KEY = "outbid.routing.key";

    public final String AUCTION_WATCHERS_KEY = "auction:watchers:";

}
