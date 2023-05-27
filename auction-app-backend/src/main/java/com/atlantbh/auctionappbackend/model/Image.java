package com.atlantbh.auctionappbackend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "product_images", schema = "auction_app_schema")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image", nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonManagedReference
    private Product product;
}
