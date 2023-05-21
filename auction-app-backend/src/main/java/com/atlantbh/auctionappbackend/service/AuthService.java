package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.exception.AppUserNotFoundException;
import com.atlantbh.auctionappbackend.exception.DuplicateAppUserException;
import com.atlantbh.auctionappbackend.model.AppUser;
import com.atlantbh.auctionappbackend.repository.AppUserRepository;
import com.atlantbh.auctionappbackend.request.LoginRequest;
import com.atlantbh.auctionappbackend.request.OAuth2LoginRequest;
import com.atlantbh.auctionappbackend.request.RegisterRequest;
import com.atlantbh.auctionappbackend.security.enums.TokenType;
import com.atlantbh.auctionappbackend.security.jwt.CustomUserDetails;
import com.atlantbh.auctionappbackend.security.oauth2.FacebookOAuth2UserInfo;
import com.atlantbh.auctionappbackend.security.oauth2.GoogleOAuth2UserInfo;
import com.atlantbh.auctionappbackend.security.oauth2.OAuth2UserInfo;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
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
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.atlantbh.auctionappbackend.utils.Constants.*;


@Service
@AllArgsConstructor
public class AuthService {
    private final AppUserRepository appUserRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    private final RestTemplate restTemplate;

    @Transactional
    public void register(RegisterRequest registerRequest) throws DuplicateAppUserException {
        if (appUserRepository.existsByEmail(registerRequest.getEmail())) {
            throw new DuplicateAppUserException();
        }

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        AppUser userToCreate = AppUser.builder().firstName(registerRequest.getFirstName()).lastName(registerRequest.getLastName()).email(registerRequest.getEmail()).password(encodedPassword).build();

        appUserRepository.save(userToCreate);
    }

    public void login(HttpServletRequest request, LoginRequest loginRequest, HttpServletResponse response) {
        AppUser loggedAppUser = appUserRepository.getByEmail(loginRequest.getEmail()).orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), loggedAppUser.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        CustomUserDetails userDetails = new CustomUserDetails(loggedAppUser.getEmail(), loggedAppUser.getPassword(), loggedAppUser.getFirstName(), loggedAppUser.getLastName(), Collections.emptyList());

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        TokenType tokenType = loginRequest.isRememberMe() ? TokenType.REMEMBER_ME : TokenType.STANDARD;
        String jwt = tokenService.generateToken(authentication, tokenType);
        boolean isSecure = request.isSecure() && !request.getServerName().equals("localhost");

        Cookie jwtCookie = new Cookie(COOKIE_NAME, jwt);
        jwtCookie.setMaxAge(COOKIE_MAX_AGE);
        jwtCookie.setHttpOnly(false);
        jwtCookie.setPath("/");
        jwtCookie.setSecure(isSecure);

        response.addCookie(jwtCookie);
    }

    public Cookie processOAuth2Login(OAuth2LoginRequest oauth2LoginRequest) {
        OAuth2UserInfo oAuth2UserInfo;

        if (PROVIDER_GOOGLE.equalsIgnoreCase(oauth2LoginRequest.getProvider())) {
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
        } else if (PROVIDER_FACEBOOK.equalsIgnoreCase(oauth2LoginRequest.getProvider())) {
            String facebookGraphApiUrl = FACEBOOK_GRAPH_API_URL + oauth2LoginRequest.getToken();
            ResponseEntity<Map<String, Object>> facebookResponse = restTemplate.exchange(facebookGraphApiUrl, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
            });
            Map<String, Object> attributes = new HashMap<>();
            Map<String, Object> body = facebookResponse.getBody();
            if (body == null) {
                throw new AppUserNotFoundException("Facebook user not found");
            }
            attributes.put("firstName", body.get(FIRST_NAME_VALUE));
            attributes.put("lastName", body.get(LAST_NAME_VALUE));
            attributes.put("sub", body.get(SUB_VALUE));
            attributes.put("email", body.get(EMAIL_VALUE));
            oAuth2UserInfo = new FacebookOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_provider"), "Invalid OAuth2 provider");
        }

        return tokenService.generateJwtCookieForOAuth2User(oAuth2UserInfo);
    }

}