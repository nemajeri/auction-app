package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.model.Bid;
import com.atlantbh.auctionappbackend.service.BidService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1/bids")
public class BidController {

    private final BidService bidService;

    @GetMapping("/app-user")
    public ResponseEntity<List<Bid>> getBidsForAppUser(@RequestParam Long userId){
        List<Bid> bids = bidService.getBidsForAppUser(userId);
        return new ResponseEntity<>(bids, OK);
    }
}
