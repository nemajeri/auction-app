package com.atlantbh.auctionappbackend.utils;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

public interface TokenVerifier {
    GoogleIdToken verifyGSIToken(String token);
}
