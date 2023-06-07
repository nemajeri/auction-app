package com.atlantbh.auctionappbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.ZonedDateTime;
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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Image> images;

    @Column(name = "start_date", nullable = false)
    private ZonedDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private ZonedDateTime endDate;

    @Formula("(SELECT COUNT(*) FROM auction_app_schema.bid b INNER JOIN auction_app_schema.product p ON p.id = b.product_id WHERE b.product_id = id)")
    private int numberOfBids;

    @Formula("(SELECT b.price FROM auction_app_schema.bid b WHERE b.product_id = id ORDER BY b.price DESC LIMIT 1)")
    private Float highestBid;

    @Column(name = "is_highlighted", nullable = false)
    private boolean isHighlighted = false;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private Subcategory subcategory;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    private String address;

    private String city;

    private String zipCode;

    private String country;

    private String phone;

    private boolean sold = false;

    public Product( String productName, String description, float startPrice, List<Image> images, ZonedDateTime startDate, ZonedDateTime endDate, Category category, Subcategory subcategory, AppUser user, String address, String city, String zipCode, String country, String phone) {
        this.productName = productName;
        this.description = description;
        this.startPrice = startPrice;
        this.images = images;
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
        this.subcategory = subcategory;
        this.user = user;
        this.address = address;
        this.city = city;
        this.zipCode = zipCode;
        this.country = country;
        this.phone = phone;
    }

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
