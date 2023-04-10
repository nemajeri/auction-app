package com.atlantbh.auctionappbackend.repository;

import com.atlantbh.auctionappbackend.dto.SubcategoryDTO;
import com.atlantbh.auctionappbackend.model.Category;
import com.atlantbh.auctionappbackend.model.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
    Set<Subcategory> findAllByCategory(Category category);
}
