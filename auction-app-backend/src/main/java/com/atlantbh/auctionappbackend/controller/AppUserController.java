package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.response.AppUserResponse;
import com.atlantbh.auctionappbackend.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/app-user")
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping("/by-email")
    public ResponseEntity<AppUserResponse> getUserByEmail(@RequestParam String email) {
        AppUserResponse appUser = appUserService.getUserByEmail(email);
        return new ResponseEntity<>(appUser, OK);
    }
}
