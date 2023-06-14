package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.response.AppUserResponse;
import com.atlantbh.auctionappbackend.service.AppUserService;
import io.swagger.annotations.*;
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
@Api(value = "AppUser Controller")
public class AppUserController {

    private final AppUserService appUserService;

    @ApiOperation(value = "Get user by email")
    @GetMapping("/by-email")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user"),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<AppUserResponse> getUserByEmail(@ApiParam(value = "Email of the user to be obtained.", required = true) @RequestParam String email) {
        AppUserResponse appUser = appUserService.getUserByEmail(email);
        return new ResponseEntity<>(appUser, OK);
    }
}