package com.atlantbh.auctionappbackend.mapper;

import com.atlantbh.auctionappbackend.dto.CategoryDTO;
import com.atlantbh.auctionappbackend.model.Category;

public class CategoryMapper {

    public CategoryDTO toCategoryDTO(Category product) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(product.getId());
        categoryDTO.setCategoryName(product.getCategoryName());
        return categoryDTO;
    }

    public Category toCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setCategoryName(categoryDTO.getCategoryName());
        return category;
    }
}
