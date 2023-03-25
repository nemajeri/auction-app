package com.atlantbh.auctionappbackend.repository;

import com.atlantbh.auctionappbackend.model.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT c.id, c.category_name, sc.id AS subcategory_id, sc.subcategory_name " +
            "FROM category c " +
            "JOIN subcategory sc ON c.id = sc.category_id " +
            "WHERE c.id = :categoryId", nativeQuery = true )
    Optional<Category> findCategoryByIdWithSubcategories(@Param("categoryId") Long categoryId);
}


