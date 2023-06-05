package com.atlantbh.auctionappbackend.repository;

import com.atlantbh.auctionappbackend.model.Bid;
import com.atlantbh.auctionappbackend.request.UserMaxBidRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findAllByUserId(Long userId, Sort sort);

    @Query("SELECT MAX(b.price) FROM Bid b WHERE b.user.id = :userId AND b.product.id = :productId")
    Optional<Float> getMaxBidFromUserForProduct(@Param("userId") Long userId, @Param("productId") Long productId);

    @Query("SELECT new com.atlantbh.auctionappbackend.request.UserMaxBidRequest(b.user.id, MAX(b.price)) FROM Bid b WHERE b.product.id = :productId GROUP BY b.user.id ORDER BY MAX(b.price) DESC")
    List<UserMaxBidRequest> findHighestBidAndUserByProduct(@Param("productId") Long productId, Pageable pageable);

}




