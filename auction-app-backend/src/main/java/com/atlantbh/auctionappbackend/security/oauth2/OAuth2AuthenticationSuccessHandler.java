package com.atlantbh.auctionappbackend.security.oauth2;

import com.atlantbh.auctionappbackend.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2UserInfo principal = (OAuth2UserInfo) authentication.getPrincipal();
        Cookie cookieWithJwtToken = tokenService.generateJwtCookieForOAuth2User(principal);
        response.addCookie(cookieWithJwtToken);
        response.sendRedirect("/");
    }
}

