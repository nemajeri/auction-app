package com.atlantbh.auctionappbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subcategory", schema = "auction_app_schema")
public class Subcategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subcategory_name", nullable = false)
    private String subCategoryName;
    @Column(name = "items_count", nullable = false)
    private int itemsCount;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
