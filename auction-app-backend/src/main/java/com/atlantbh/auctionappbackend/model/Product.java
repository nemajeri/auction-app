package com.atlantbh.auctionappbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "start_price", nullable = false)
    private Float startPrice;

    @Column(name = "images", nullable = false)
    private String images;

    @Column(name = "subcategory_id", nullable = false)
    private Subcategory subcategory;
}
