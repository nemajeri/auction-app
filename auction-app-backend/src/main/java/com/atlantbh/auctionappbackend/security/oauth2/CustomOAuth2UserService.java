package com.atlantbh.auctionappbackend.security.oauth2;

import com.atlantbh.auctionappbackend.security.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;

        switch (userRequest.getClientRegistration().getRegistrationId()) {
            case "google":
                oAuth2UserInfo = new GoogleOAuth2UserInfo(oAuth2User.getAttributes());
                break;
            case "facebook":
                oAuth2UserInfo = new FacebookOAuth2UserInfo(oAuth2User.getAttributes());
                break;
            default:
                throw new OAuth2AuthenticationException(new OAuth2Error("invalid_provider"), "Invalid OAuth2 provider");
        }

        String jwt = jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(oAuth2UserInfo.getEmail(), ""));
        Map<String, Object> additionalInfo = new HashMap<>(oAuth2User.getAttributes());
        additionalInfo.put("jwt", jwt);
        OAuth2User oAuth2UserWithJwt = new DefaultOAuth2User(oAuth2User.getAuthorities(), additionalInfo, "jwt");
        return oAuth2UserWithJwt;
    }
}
