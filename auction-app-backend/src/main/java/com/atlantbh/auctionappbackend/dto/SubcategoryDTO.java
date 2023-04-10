package com.atlantbh.auctionappbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubcategoryDTO {
    private Long id;

    private String subCategoryName;

    private Integer numberOfProducts;

    private Long categoryId;

}
