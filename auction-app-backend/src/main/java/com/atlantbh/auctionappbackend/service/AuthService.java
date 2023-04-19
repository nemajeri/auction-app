package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.exception.DuplicateAppUserException;
import com.atlantbh.auctionappbackend.model.AppUser;
import com.atlantbh.auctionappbackend.repository.AppUserRepository;
import com.atlantbh.auctionappbackend.request.LoginRequest;
import com.atlantbh.auctionappbackend.request.OAuth2LoginRequest;
import com.atlantbh.auctionappbackend.request.RegisterRequest;
import com.atlantbh.auctionappbackend.security.jwt.CustomUserDetails;
import com.atlantbh.auctionappbackend.security.oauth2.FacebookOAuth2UserInfo;
import com.atlantbh.auctionappbackend.security.oauth2.GoogleOAuth2UserInfo;
import com.atlantbh.auctionappbackend.security.oauth2.OAuth2UserInfo;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final AppUserRepository appUserRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    private final RestTemplate restTemplate;

    public void register(RegisterRequest registerRequest) throws DuplicateAppUserException {
        if (appUserRepository.existsByEmail(registerRequest.getEmail())) {
            throw new DuplicateAppUserException();
        }

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        AppUser userToCreate = new AppUser(
                null,
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                registerRequest.getEmail(),
                encodedPassword
        );

        appUserRepository.save(userToCreate);
    }

    public void login(HttpServletRequest request, LoginRequest loginRequest, HttpServletResponse response) {
        AppUser loggedAppUser = appUserRepository.getByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), loggedAppUser.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String firstName = loggedAppUser.getFirstName();
        String lastName = loggedAppUser.getLastName();
        CustomUserDetails userDetails = new CustomUserDetails(loggedAppUser.getEmail(), loggedAppUser.getPassword(), firstName, lastName, Collections.emptyList());

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        String jwt = tokenService.generateToken(authentication);
        boolean isSecure = request.isSecure() && !request.getServerName().equals("localhost");

        Cookie jwtCookie = new Cookie("auction_app_token", jwt);
        jwtCookie.setMaxAge(4 * 60 * 60);
        jwtCookie.setHttpOnly(false);
        jwtCookie.setPath("/");
        jwtCookie.setSecure(isSecure);

        response.addCookie(jwtCookie);
    }

    public Cookie processOAuth2Login(OAuth2LoginRequest oauth2LoginRequest) {
        OAuth2UserInfo oAuth2UserInfo = new GoogleOAuth2UserInfo(Collections.emptyMap());

        if ("google".equalsIgnoreCase(oauth2LoginRequest.getProvider())) {
            GoogleIdToken googleIdToken = tokenService.verifyGSIToken(oauth2LoginRequest.getToken());
            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("firstName", firstName);
            attributes.put("lastName", lastName);
            attributes.put("sub", payload.getSubject());
            attributes.put("email", payload.getEmail());
            oAuth2UserInfo = new GoogleOAuth2UserInfo(attributes);
        } else if ("facebook".equalsIgnoreCase(oauth2LoginRequest.getProvider())) {
            String facebookGraphApiUrl = "https://graph.facebook.com/me?fields=id,email,first_name,last_name&access_token=" + oauth2LoginRequest.getToken();
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

        return tokenService.generateJwtCookieForOAuth2User(oAuth2UserInfo);
    }

}