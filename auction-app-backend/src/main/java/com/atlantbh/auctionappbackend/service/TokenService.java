package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.model.AppUser;
import com.atlantbh.auctionappbackend.repository.AppUserRepository;
import com.atlantbh.auctionappbackend.security.enums.TokenType;
import com.atlantbh.auctionappbackend.security.jwt.CustomUserDetails;
import com.atlantbh.auctionappbackend.security.oauth2.OAuth2UserInfo;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.atlantbh.auctionappbackend.utils.Constants.*;

@Slf4j
@Service
public class TokenService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${app:jwtSecret}")
    private String jwtSecret;

    private final AppUserRepository appUserRepository;

    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    @Autowired
    public TokenService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public GoogleIdToken verifyGSIToken(String token) {
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

    public Cookie generateJwtCookieForOAuth2User(OAuth2UserInfo oAuth2UserInfo) {
        String email = oAuth2UserInfo.getEmail();
        AppUser appUser = appUserRepository.findByEmail(email);
        if (Objects.equals(appUser, null)) {
            appUser = new AppUser();
            appUser.setEmail(email);
            appUser.setFirstName(oAuth2UserInfo.getFirstName());
            appUser.setLastName(oAuth2UserInfo.getLastName());
            appUser.setPassword("");
            appUserRepository.save(appUser);
        }

        String firstName = oAuth2UserInfo.getFirstName();
        String lastName = oAuth2UserInfo.getLastName();
        CustomUserDetails customUserDetails = new CustomUserDetails(email, "", firstName, lastName, Collections.emptyList());

        String jwt = generateToken(new UsernamePasswordAuthenticationToken(customUserDetails, ""), TokenType.STANDARD);
        Cookie cookie = new Cookie(COOKIE_NAME, jwt);
        cookie.setPath("/");
        cookie.setMaxAge(COOKIE_MAX_AGE);
        return cookie;
    }

    public String generateToken(Authentication authentication, TokenType tokenType) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = userDetails.getEmail();
        String firstName = userDetails.getFirstName();
        String lastName = userDetails.getLastName();

        Instant now = Instant.now();
        Instant expiration = now.plus(tokenType.getDuration(), tokenType.getUnit());

        return Jwts.builder()
                .setSubject(email)
                .claim("firstName", firstName)
                .claim("lastName", lastName)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getClaimFromToken(String token, String claim) {
        Claims claims = Jwts
                .parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.get(claim, String.class);
    }

    public boolean validateToken(String token) {
        if (blacklistedTokens.contains(token)) {
            return false;
        }

        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    public String getJwtFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(COOKIE_NAME)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public void invalidateToken(String token) {
        blacklistedTokens.add(token);
    }

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
