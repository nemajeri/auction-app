package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.dto.SubcategoryDTO;
import com.atlantbh.auctionappbackend.exception.CategoryNotFoundException;
import com.atlantbh.auctionappbackend.service.SubcategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/subcategories")
public class SubcategoryController {

    private final SubcategoryService subcategoryService;

    @GetMapping("/category")
    public ResponseEntity<Set<SubcategoryDTO>> getSubcategoriesForCategory(@RequestParam Long categoryId) {
        try {
            return ResponseEntity.ok(subcategoryService.getSubcategoriesForCategory(categoryId));
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
