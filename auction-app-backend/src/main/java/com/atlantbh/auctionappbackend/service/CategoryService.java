package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.model.Category;
import com.atlantbh.auctionappbackend.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {

        List<Category> categories = categoryRepository.findAll();

        if (categories.isEmpty())
            return new ArrayList<>() {
            };

        return categories;
    }
}
