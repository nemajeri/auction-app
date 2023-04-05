package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.response.ProductsResponse;
import com.atlantbh.auctionappbackend.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/products")
public class ProductController {

    private ProductService productService;

    @GetMapping("/items")
    public ResponseEntity<Page<ProductsResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "9") Integer pageSize,
            @RequestParam(defaultValue = "") String searchTerm,
            @RequestParam(required = false) Long categoryId) {

        Page<ProductsResponse> productsList;
        if (categoryId != null) {
            productsList = productService.getAllProductsFromCategory(pageNumber, pageSize, categoryId);
        } else {
            productsList = productService.getAllProductsBySearchTerm(pageNumber, pageSize, searchTerm);
        }

        return ResponseEntity.ok(productsList);
    }

}
