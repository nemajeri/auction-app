package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.request.LoginRequest;
import com.atlantbh.auctionappbackend.request.OAuth2LoginRequest;
import com.atlantbh.auctionappbackend.request.RegisterRequest;
import com.atlantbh.auctionappbackend.service.AuthService;
import com.atlantbh.auctionappbackend.service.TokenService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.atlantbh.auctionappbackend.utils.Constants.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private TokenService tokenService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @ApiOperation(value = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User registered successfully"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PostMapping(value = "/register")
    public ResponseEntity<Object> register( @Valid @RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return new ResponseEntity<>(CREATED);
    }

    @ApiOperation(value = "User login")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Login successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized")
    })
    @PostMapping(value = "/login")
    public ResponseEntity<String> login( @Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response, HttpServletRequest request) {
        authService.login(request, loginRequest, response);
        return new ResponseEntity<>("Login successful", OK);
    }

    @ApiOperation(value = "User logout")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Logout successful"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String token = tokenService.getJwtFromCookie(request);
        tokenService.invalidateToken(token);

        Cookie cookie = new Cookie(COOKIE_NAME, "");
        cookie.setMaxAge(LOGOUT_COOKIE_MAX_AGE);
        cookie.setPath("/");
        response.addCookie(cookie);

        return new ResponseEntity<>(OK);
    }

    @ApiOperation(value = "User login with Google or Facebook")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Logout successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Unauthorized")
    })
    @PostMapping("/oauth2-login-success")
    public ResponseEntity<Void> handleOAuth2LoginSuccess(@RequestBody OAuth2LoginRequest oauth2LoginRequest, HttpServletResponse response) {
        Cookie cookieWithJwtToken = authService.processOAuth2Login(oauth2LoginRequest);
        response.addCookie(cookieWithJwtToken);
        return new ResponseEntity<>(OK);
    }

}
