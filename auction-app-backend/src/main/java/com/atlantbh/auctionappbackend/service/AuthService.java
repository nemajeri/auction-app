package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.exception.DuplicateAppUserException;
import com.atlantbh.auctionappbackend.model.AppUser;
import com.atlantbh.auctionappbackend.repository.AppUserRepository;
import com.atlantbh.auctionappbackend.request.LoginRequest;
import com.atlantbh.auctionappbackend.request.RegisterRequest;
import com.atlantbh.auctionappbackend.security.jwt.CustomUserDetails;
import com.atlantbh.auctionappbackend.security.jwt.JwtTokenProvider;
import com.atlantbh.auctionappbackend.security.oauth2.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AppUserRepository appUserRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

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

        CustomUserDetails userDetails = new CustomUserDetails(loggedAppUser.getEmail(), loggedAppUser.getPassword(),  Collections.emptyList());

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        String jwt =  jwtTokenProvider.generateToken(authentication);
        boolean isSecure = request.isSecure() && !request.getServerName().equals("localhost");

        Cookie jwtCookie = new Cookie("jwt" , jwt);
        jwtCookie.setMaxAge(4 * 60 * 60);
        jwtCookie.setHttpOnly(false);
        jwtCookie.setPath("/");
        jwtCookie.setSecure(isSecure);

        response.addCookie(jwtCookie);
    }



    public Cookie generateJwtCookieForOAuth2User(OAuth2UserInfo oAuth2UserInfo) {
        String email = oAuth2UserInfo.getEmail();
        AppUser appUser = appUserRepository.findByEmail(email);
        if (appUser == null) {
            appUser = new AppUser();
            appUser.setEmail(email);
            appUser.setFirstName(oAuth2UserInfo.getFirstName());
            appUser.setLastName(oAuth2UserInfo.getLastName());
            appUser.setPassword("");
            appUserRepository.save(appUser);
        }

        CustomUserDetails customUserDetails = new CustomUserDetails(email, "", Collections.emptyList());

        String jwt = jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(customUserDetails, ""));
        Cookie cookie = new Cookie("jwt-token", jwt);
        cookie.setPath("/");
        cookie.setMaxAge(4 * 60 * 60);
        return cookie;
    }

}
