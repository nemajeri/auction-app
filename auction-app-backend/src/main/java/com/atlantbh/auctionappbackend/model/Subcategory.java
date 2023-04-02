package com.atlantbh.auctionappbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subcategory", schema = "auction_app_schema")
public class Subcategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subcategory_name", nullable = false)
    private String subCategoryName;

    @Formula("(SELECT COUNT(*) FROM auction_app_schema.product p WHERE p.category_id = id)")
    private Integer numberOfProducts;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
