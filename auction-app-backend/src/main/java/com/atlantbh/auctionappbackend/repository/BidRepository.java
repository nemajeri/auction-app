package com.atlantbh.auctionappbackend.repository;

import com.atlantbh.auctionappbackend.model.Bid;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findAllByUserId(Long userId, Sort sort);

}




