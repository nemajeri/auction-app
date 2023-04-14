package com.atlantbh.auctionappbackend.security.oauth2;

import java.util.Map;

public class FacebookOAuth2UserInfo implements OAuth2UserInfo {
    private final Map<String, Object> attributes;

    public FacebookOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getFirstName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getLastName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
}

