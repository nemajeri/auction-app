package com.atlantbh.auctionappbackend.service;
import com.atlantbh.auctionappbackend.exception.AppUserNotFoundException;
import com.atlantbh.auctionappbackend.model.Bid;
import com.atlantbh.auctionappbackend.repository.BidRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BidService {

    private final BidRepository bidRepository;

    public List<Bid> getBidsForAppUser(Long userId) {
        List<Bid> bids = bidRepository.findAllByUserId(userId, Sort.by(Sort.Direction.DESC, "bidDate"));
        if(bids.isEmpty()) {
            throw new AppUserNotFoundException("Bids from user with id: " + userId + " not found");
        }
        return bids;
    }
}
