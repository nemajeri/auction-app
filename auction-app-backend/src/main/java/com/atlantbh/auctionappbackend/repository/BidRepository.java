package com.atlantbh.auctionappbackend.repository;

import com.atlantbh.auctionappbackend.model.Bid;
import com.atlantbh.auctionappbackend.dto.UserMaxBidRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findAllByUserId(Long userId, Sort sort);

    @Query("SELECT new com.atlantbh.auctionappbackend.dto.UserMaxBidRecord(b.user.id, MAX(b.price)) FROM Bid b WHERE b.product.id = :productId GROUP BY b.user.id ORDER BY MAX(b.price) DESC")
    List<UserMaxBidRecord> findHighestBidAndUserByProduct(@Param("productId") Long productId, Pageable pageable);

}




