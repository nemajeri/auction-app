package com.atlantbh.auctionappbackend.repository;

import com.atlantbh.auctionappbackend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByCategoryId(Long categoryId, Pageable paging);

    Page<Product> findByProductNameContainingIgnoreCase(String searchTerm, Pageable paging);
}
