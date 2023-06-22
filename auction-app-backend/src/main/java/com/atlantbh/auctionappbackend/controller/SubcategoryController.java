package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.model.Subcategory;
import com.atlantbh.auctionappbackend.service.SubcategoryService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/subcategories")
@Api(value = "Subcategories", tags = "Subcategory Controller")
public class SubcategoryController {

    private final SubcategoryService subcategoryService;

    @GetMapping("/category")
    @ApiOperation(value = "Get subcategories for a given category")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved subcategories for the category"),
            @ApiResponse(code = 400, message = "Invalid category id"),
            @ApiResponse(code = 404, message = "Category not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<Set<Subcategory>> getSubcategoriesForCategory(
            @ApiParam(value = "The id of the category to retrieve subcategories for", required = true)
            @RequestParam Long categoryId) {
        return ResponseEntity.ok(subcategoryService.getSubcategoriesForCategory(categoryId));
    }
}