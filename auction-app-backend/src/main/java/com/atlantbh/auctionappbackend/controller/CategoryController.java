package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.dto.CategoryDTO;
import com.atlantbh.auctionappbackend.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping()
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}