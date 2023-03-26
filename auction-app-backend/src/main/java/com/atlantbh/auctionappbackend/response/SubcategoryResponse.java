package com.atlantbh.auctionappbackend.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubcategoryResponse implements Comparable<SubcategoryResponse> {
    private Long id;
    private String subCategoryName;
    @Override
    public int compareTo(SubcategoryResponse o) {
        return this.subCategoryName.compareTo(o.subCategoryName);
    }
}
