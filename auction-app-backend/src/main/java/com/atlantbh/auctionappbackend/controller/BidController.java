package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.request.BidRequest;
import com.atlantbh.auctionappbackend.response.AppUserBidsResponse;
import com.atlantbh.auctionappbackend.service.BidService;
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
public class BidController {

    private final BidService bidService;

    @GetMapping("/app-user")
    public ResponseEntity<List<AppUserBidsResponse>> getBidsForAppUser(HttpServletRequest request) {
        List<AppUserBidsResponse> bids = bidService.getBidsForAppUser(request);
        return new ResponseEntity<>(bids, OK);
    }

    @PostMapping("/create-bid")
    public ResponseEntity<Void> createBid(@Valid @RequestBody BidRequest bidRequest, HttpServletRequest request) {
        bidService.createBidAndPublishToQueue(bidRequest.getProductId(), bidRequest.getAmount(), request);
        return new ResponseEntity<>(CREATED);
    }
}
