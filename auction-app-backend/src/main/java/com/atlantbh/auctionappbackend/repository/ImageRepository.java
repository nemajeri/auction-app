package com.atlantbh.auctionappbackend.repository;

import com.atlantbh.auctionappbackend.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
