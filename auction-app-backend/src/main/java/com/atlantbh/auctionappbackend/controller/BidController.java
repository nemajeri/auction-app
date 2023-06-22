package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.request.BidRequest;
import com.atlantbh.auctionappbackend.response.AppUserBidsResponse;
import com.atlantbh.auctionappbackend.service.BidService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/bid")
@Api(value = "Bid Controller")
public class BidController {

    private final BidService bidService;

    @GetMapping("/app-user")
    @ApiOperation(value = "Get bids made by user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved bids made by user"),
            @ApiResponse(code = 400, message = "Invalid request or user not authenticated"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<List<AppUserBidsResponse>> getBidsForAppUser(HttpServletRequest request) {
        List<AppUserBidsResponse> bids = bidService.getBidsForAppUser(request);
        return new ResponseEntity<>(bids, OK);
    }

    @ApiOperation(value = "Create a new bid")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Bid successfully created"),
            @ApiResponse(code = 400, message = "Invalid request, bid amount must be higher than current highest bid, auction must be active, and user cannot bid on their own product"),
            @ApiResponse(code = 404, message = "Product or user not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping("/create-bid")
    public ResponseEntity<Void> createBid(@Valid @RequestBody BidRequest bidRequest, HttpServletRequest request) {
        bidService.createBidAndPublishToQueue(bidRequest.getProductId(), bidRequest.getAmount(), request);
        return new ResponseEntity<>(CREATED);
    }
}
