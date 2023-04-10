package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.dto.SubcategoryDTO;
import com.atlantbh.auctionappbackend.exception.CategoryNotFoundException;
import com.atlantbh.auctionappbackend.model.Category;
import com.atlantbh.auctionappbackend.model.Subcategory;
import com.atlantbh.auctionappbackend.repository.CategoryRepository;
import com.atlantbh.auctionappbackend.repository.SubcategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SubcategoryService {

    private final CategoryRepository categoryRepository;

    private final SubcategoryRepository subcategoryRepository;

    public Set<SubcategoryDTO> getSubcategoriesForCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Wrong category id"));

        Set<Subcategory> subcategories = subcategoryRepository.findAllByCategory(category);

        Set<SubcategoryDTO> response = subcategories.stream()
                .map(subcategory -> new SubcategoryDTO(subcategory.getId(), subcategory.getSubCategoryName(), subcategory.getNumberOfProducts() ,subcategory.getCategory().getId()))
                .collect(Collectors.toSet());

        return response;
    }
}
