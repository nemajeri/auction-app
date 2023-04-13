package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.request.LoginRequest;
import com.atlantbh.auctionappbackend.request.RegisterRequest;
import com.atlantbh.auctionappbackend.security.jwt.JwtAuthenticationFilter;
import com.atlantbh.auctionappbackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

}
