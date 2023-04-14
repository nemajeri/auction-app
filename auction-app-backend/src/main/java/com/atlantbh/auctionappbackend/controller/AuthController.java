package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.request.LoginRequest;
import com.atlantbh.auctionappbackend.request.RegisterRequest;
import com.atlantbh.auctionappbackend.security.jwt.JwtAuthenticationFilter;
import com.atlantbh.auctionappbackend.security.oauth2.FacebookOAuth2UserInfo;
import com.atlantbh.auctionappbackend.security.oauth2.GoogleOAuth2UserInfo;
import com.atlantbh.auctionappbackend.security.oauth2.OAuth2UserInfo;
import com.atlantbh.auctionappbackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final JwtAuthenticationFilter jwtFilter;

    @PostMapping(value = "/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest registerRequest) {
            authService.register(registerRequest);
            return new ResponseEntity<>(CREATED);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Cookie> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
            Cookie cookieWithJwtToken = authService.login(loginRequest, response);
            response.addCookie(cookieWithJwtToken);
            return new ResponseEntity<>(OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String token = jwtFilter.getJwtFromCookie(request);
        jwtFilter.invalidateToken(token);

        Cookie cookie = new Cookie("jwt-token", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return new ResponseEntity<>(OK);
    }

    @PostMapping("/oauth2-login-success")
    public ResponseEntity<Cookie> handleOAuth2LoginSuccess(@AuthenticationPrincipal OAuth2User principal, HttpServletResponse response) {
        OAuth2UserInfo oAuth2UserInfo;
        String registrationId = principal.getAttribute("registrationId");

        switch (registrationId) {
            case "google":
                oAuth2UserInfo = new GoogleOAuth2UserInfo(principal.getAttributes());
                break;
            case "facebook":
                oAuth2UserInfo = new FacebookOAuth2UserInfo(principal.getAttributes());
                break;
            default:
                throw new OAuth2AuthenticationException(new OAuth2Error("invalid_provider"), "Invalid OAuth2 provider");
        }
        Cookie cookieWithJwtToken = authService.generateJwtCookieForOAuth2User(oAuth2UserInfo);
        response.addCookie(cookieWithJwtToken);
        return new ResponseEntity<>(OK);
    }


}
