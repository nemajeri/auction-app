package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.request.LoginRequest;
import com.atlantbh.auctionappbackend.request.RegisterRequest;
import com.atlantbh.auctionappbackend.security.jwt.JwtAuthenticationFilter;
import com.atlantbh.auctionappbackend.security.oauth2.FacebookOAuth2UserInfo;
import com.atlantbh.auctionappbackend.security.oauth2.GoogleOAuth2UserInfo;
import com.atlantbh.auctionappbackend.security.oauth2.OAuth2UserInfo;
import com.atlantbh.auctionappbackend.service.AuthService;
import com.fasterxml.jackson.core.JsonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final JwtAuthenticationFilter jwtFilter;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @PostMapping(value = "/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return new ResponseEntity<>(CREATED);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response, HttpServletRequest request) {
        authService.login(request, loginRequest, response);
        return new ResponseEntity<>("Login successful", OK);
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
    public ResponseEntity<Cookie> handleOAuth2LoginSuccess(@RequestParam String provider, @RequestParam String token, HttpServletResponse response) {
        OAuth2UserInfo oAuth2UserInfo = new GoogleOAuth2UserInfo(Collections.emptyMap());
        RestTemplate restTemplate = new RestTemplate();

        if ("google".equalsIgnoreCase(provider)) {
            GoogleIdToken googleIdToken = verifyGISToken(token);
            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("firstName", firstName);
            attributes.put("lastName", lastName);
            attributes.put("sub", payload.getSubject());
            attributes.put("email", payload.getEmail());
            oAuth2UserInfo = new GoogleOAuth2UserInfo(attributes);
        } else if ("facebook".equalsIgnoreCase(provider)) {
            String facebookGraphApiUrl = "https://graph.facebook.com/me?fields=id,email,first_name,last_name&access_token=" + token;
            ResponseEntity<Map> facebookResponse = restTemplate.getForEntity(facebookGraphApiUrl, Map.class);
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("firstName", facebookResponse.getBody().get("first_name"));
            attributes.put("lastName", facebookResponse.getBody().get("last_name"));
            attributes.put("sub", facebookResponse.getBody().get("id"));
            attributes.put("email", facebookResponse.getBody().get("email"));
            oAuth2UserInfo = new FacebookOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_provider"), "Invalid OAuth2 provider");
        }

        Cookie cookieWithJwtToken = authService.generateJwtCookieForOAuth2User(oAuth2UserInfo);
        response.addCookie(cookieWithJwtToken);
        return new ResponseEntity<>(OK);
    }

    private GoogleIdToken verifyGISToken(String token) {
        try {
            HttpTransport transport = new NetHttpTransport();
            JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken googleIdToken = verifier.verify(token);
            if (googleIdToken != null) {
                return googleIdToken;
            } else {
                throw new OAuth2AuthenticationException(new OAuth2Error("invalid_token"), "Invalid GSI token");
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new OAuth2AuthenticationException(new OAuth2Error("token_verification_error"), "Error verifying GSI token", e);
        }
    }

}
