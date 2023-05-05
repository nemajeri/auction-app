package com.atlantbh.auctionappbackend.repository;

import com.atlantbh.auctionappbackend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @Query("SELECT p " +
            "FROM Product p LEFT JOIN p.images pi " +
            "WHERE p.startDate <= CURRENT_TIMESTAMP AND p.endDate > CURRENT_TIMESTAMP " +
            "GROUP BY p.id " +
            "ORDER BY p.startDate DESC")
    Page<Product> getNewArrivalsProducts(Pageable pageable);

    @Query("SELECT p " +
            "FROM Product p LEFT JOIN p.images pi " +
            "WHERE p.startDate <= CURRENT_TIMESTAMP AND p.endDate > CURRENT_TIMESTAMP " +
            "GROUP BY p.id " +
            "ORDER BY p.endDate ASC")
    Page<Product> getLastChanceProducts(Pageable pageable);

    @Query("SELECT p.productName FROM Product p WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<String> findTopNamesByNameSimilarity(@Param("query") String query);

    List<Product> findAllProductsByUserIdAndEndDateIsAfter(Long userId, LocalDateTime time, Sort sort);

    List<Product> findAllProductsByUserIdAndEndDateIsBefore(Long userId, LocalDateTime time, Sort sort);
}

