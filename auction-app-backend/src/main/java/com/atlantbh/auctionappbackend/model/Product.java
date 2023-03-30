package com.atlantbh.auctionappbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "product", schema="auction_app_schema")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "start_price", nullable = false)
    private Float startPrice;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image")
    private List<String> images;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Formula("(SELECT COUNT(*) FROM auction_app_schema.bid b INNER JOIN auction_app_schema.product p ON p.id = b.product_id WHERE b.product_id = p.id)")
    private BigInteger numberOfBids;

    @Formula("(SELECT b.price FROM auction_app_schema.product p INNER JOIN auction_app_schema.bid b ON p.id =b.product_id ORDER BY b.price DESC LIMIT 1)")
    private BigInteger highestBid;

}

