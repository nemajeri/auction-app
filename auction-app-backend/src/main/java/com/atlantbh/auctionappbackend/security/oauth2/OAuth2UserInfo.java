package com.atlantbh.auctionappbackend.security.oauth2;

public interface OAuth2UserInfo {

    String getId();

    String getFirstName();

    String getLastName();

    String getEmail();
}
