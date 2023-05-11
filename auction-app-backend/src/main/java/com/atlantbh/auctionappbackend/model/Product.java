package com.atlantbh.auctionappbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product", schema = "auction_app_schema")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "start_price", nullable = false)
    private float startPrice;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image")
    private List<String> images;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Formula("(SELECT COUNT(*) FROM auction_app_schema.bid b INNER JOIN auction_app_schema.product p ON p.id = b.product_id WHERE b.product_id = id)")
    private int numberOfBids;

    @Formula("(SELECT b.price FROM auction_app_schema.bid b WHERE b.product_id = id ORDER BY b.price DESC LIMIT 1)")
    private Float highestBid;

    @Column(name = "is_highlighted", nullable = false)
    private boolean isHighlighted;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private Subcategory subcategory;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "country")
    private String country;

    @Column(name = "phone")
    private String phone;

    private boolean sold;

    public Float getHighestBid() {
        if (highestBid == null && numberOfBids == 0) {
            return this.startPrice;
        }
        return this.highestBid;
    }

    public int getNumberOfBids() {
        if (highestBid == null && numberOfBids == 0) {
            return 0;
        }
        return this.numberOfBids;
    }

    public boolean isOwner(String email) {
        return this.user.getEmail().equals(email);
    }

}
