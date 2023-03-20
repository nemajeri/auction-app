package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.dto.CategoryDTO;
import com.atlantbh.auctionappbackend.dto.ProductDTO;
import com.atlantbh.auctionappbackend.model.Category;
import com.atlantbh.auctionappbackend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public List<CategoryDTO> getAllCategories() {

       List<Category> categories =  categoryRepository.findAll();

        if (categories.isEmpty())
            return new ArrayList<>() {
            };

        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<CategoryDTO>>() {
        }.getType();
        List<CategoryDTO> categoryDTOs = modelMapper.map(categories, listType);

        return categoryDTOs;
    }
}
