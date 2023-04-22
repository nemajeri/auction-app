package com.atlantbh.auctionappbackend.controller;

<<<<<<< HEAD
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
=======
import com.atlantbh.auctionappbackend.request.BidRequest;
import com.atlantbh.auctionappbackend.service.BidService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/bid")
>>>>>>> 248d757 (Added bidding logic in backend)
public class BidController {

    private final BidService bidService;

<<<<<<< HEAD
    @GetMapping("/app-user")
    public ResponseEntity<List<Bid>> getBidsForAppUser(@RequestParam Long userId){
        List<Bid> bids = bidService.getBidsForAppUser(userId);
        return new ResponseEntity<>(bids, OK);
    }
}
=======
    @PostMapping("/create-bid")
    public ResponseEntity<?> createBid(@Valid @RequestBody BidRequest bidRequest, HttpServletRequest request){
        bidService.createBid(bidRequest.getProductId(), bidRequest.getAmount(), request);
        return new ResponseEntity<>(CREATED);
    }
}

>>>>>>> 248d757 (Added bidding logic in backend)
