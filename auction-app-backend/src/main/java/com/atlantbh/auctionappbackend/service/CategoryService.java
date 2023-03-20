package com.atlantbh.auctionappbackend.service.serviceImpl;

import com.atlantbh.auctionappbackend.model.Category;
import com.atlantbh.auctionappbackend.repository.CategoryRepository;
import com.atlantbh.auctionappbackend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;


    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
}
