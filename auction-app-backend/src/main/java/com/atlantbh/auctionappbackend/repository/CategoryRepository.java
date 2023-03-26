package com.atlantbh.auctionappbackend.repository;

import com.atlantbh.auctionappbackend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}



