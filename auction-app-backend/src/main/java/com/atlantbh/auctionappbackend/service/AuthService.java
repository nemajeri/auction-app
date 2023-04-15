package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.exception.DuplicateAppUserException;
import com.atlantbh.auctionappbackend.model.AppUser;
import com.atlantbh.auctionappbackend.repository.AppUserRepository;
import com.atlantbh.auctionappbackend.request.LoginRequest;
import com.atlantbh.auctionappbackend.request.RegisterRequest;
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
import java.util.Optional;

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
        Optional<AppUser> appUser = appUserRepository.getByEmail(loginRequest.getEmail());

        if (!appUser.isPresent()) {
            throw new BadCredentialsException("Invalid email or password");
        }

        AppUser loggedAppUser = appUser.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), loggedAppUser.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(loggedAppUser.getEmail(), loggedAppUser.getPassword());
        String jwt =  jwtTokenProvider.generateToken(authentication);
        boolean isSecure = !request.isSecure() || request.getServerName().equals("localhost") ? false : true;

        Cookie jwtCookie = new Cookie("jwt" , jwt);
        jwtCookie.setMaxAge(4 * 60 * 60);
        jwtCookie.setHttpOnly(true);
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

        String jwt = jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(email, ""));
        Cookie cookie = new Cookie("jwt-token", jwt);
        cookie.setPath("/");
        cookie.setMaxAge(4 * 60 * 60);
        return cookie;
    }

}
