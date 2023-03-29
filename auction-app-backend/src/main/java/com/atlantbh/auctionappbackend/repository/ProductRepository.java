package com.atlantbh.auctionappbackend.repository;

import com.atlantbh.auctionappbackend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository()
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT p.id, p.product_name, p.start_price, p.description, p.images, p.is_highlighted " +
            "FROM product p " +
            "WHERE start_date <= now() AND end_date > now() " +
            "ORDER BY start_date DESC " +
            "LIMIT ? OFFSET ?",
            nativeQuery = true)
    List<Product> getNewArrivalsProducts(int limit, int offset);

    @Query(value = "SELECT p.id, p.product_name, p.start_price, p.description, p.images, p.is_highlighted " +
            "FROM product p " +
            "WHERE start_date <= now() AND end_date > now() " +
            "ORDER BY end_date " +
            "LIMIT ? OFFSET ?",
            nativeQuery = true)
    List<Product> getLastChanceProducts(int limit, int offset);

}
