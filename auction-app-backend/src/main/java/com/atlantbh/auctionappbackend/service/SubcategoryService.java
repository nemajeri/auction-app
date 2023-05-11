package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.exception.CategoryNotFoundException;
import com.atlantbh.auctionappbackend.model.Category;
import com.atlantbh.auctionappbackend.model.Subcategory;
import com.atlantbh.auctionappbackend.repository.CategoryRepository;
import com.atlantbh.auctionappbackend.repository.SubcategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor
@Service
public class SubcategoryService {

    private final CategoryRepository categoryRepository;

    private final SubcategoryRepository subcategoryRepository;

    public Set<Subcategory> getSubcategoriesForCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Wrong category id"));

        Set<Subcategory> subcategories = subcategoryRepository.findAllByCategory(category);

        return subcategories;
    }
}
