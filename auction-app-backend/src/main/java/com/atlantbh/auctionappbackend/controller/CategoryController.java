package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.model.Category;
import com.atlantbh.auctionappbackend.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/categories")
@Api("Category Controller")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping()
    @ApiOperation(value = "Get all categories")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all categories"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

}
