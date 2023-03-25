package com.atlantbh.auctionappbackend.repository;

import com.atlantbh.auctionappbackend.model.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
    @Query(value = "SELECT COUNT(*) FROM product WHERE subcategory_id = :subcategoryId", nativeQuery = true)
    int countItemsForSubcategory(@Param("subcategoryId") Long subcategoryId);
}

