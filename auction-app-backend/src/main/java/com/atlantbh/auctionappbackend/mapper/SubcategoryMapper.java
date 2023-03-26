package com.atlantbh.auctionappbackend.mapper;

import com.atlantbh.auctionappbackend.dto.SubcategoryDTO;
import com.atlantbh.auctionappbackend.model.Subcategory;
import org.springframework.stereotype.Component;

@Component
public class SubcategoryMapper {

    public SubcategoryDTO toSubcategoryDTO(Subcategory subcategory) {
        SubcategoryDTO subcategoryDTO = new SubcategoryDTO();
        subcategoryDTO.setId(subcategory.getId());
        subcategoryDTO.setSubCategoryName(subcategory.getSubCategoryName());
        return subcategoryDTO;
    }

    public Subcategory toSubcategory(SubcategoryDTO subcategoryDTO) {
        Subcategory subcategory = new Subcategory();
        subcategory.setId(subcategoryDTO.getId());
        subcategory.setSubCategoryName(subcategoryDTO.getSubCategoryName());
        return subcategory;
    }
}
