package com.atlantbh.auctionappbackend.utils;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

public class TestGoogleIdToken extends GoogleIdToken {

    public TestGoogleIdToken(Payload payload) {
        super(new Header(), payload, new byte[0], new byte[0]);
    }
}
