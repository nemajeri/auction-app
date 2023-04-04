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
    public ResponseEntity<List<ProductsResponse>> getAllProducts(
            @RequestParam(defaultValue = "") String searchTerm) {
        List<ProductsResponse> productsList = productService.getAllProducts(searchTerm);

        return ResponseEntity.ok(productsList);
    }

    @GetMapping("/items/category")
    public ResponseEntity<Page<ProductsResponse>> getAllProductsFromCategory(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "9") Integer pageSize,
            @RequestParam Long categoryId) {
        Page<ProductsResponse> productsList = productService.getAllProductsFromCategory(pageNumber, pageSize, categoryId);

        return ResponseEntity.ok(productsList);
    }

}
