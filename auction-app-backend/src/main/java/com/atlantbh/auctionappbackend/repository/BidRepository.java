package com.atlantbh.auctionappbackend.repository;

import com.atlantbh.auctionappbackend.model.Bid;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findAllByUserId(Long userId, Sort sort);
}
