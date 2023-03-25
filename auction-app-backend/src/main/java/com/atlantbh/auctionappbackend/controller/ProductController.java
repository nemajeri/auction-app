package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.model.Product;
import com.atlantbh.auctionappbackend.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/products")
public class ProductController {

    private ProductService productService;

    @GetMapping("/items")
    public ResponseEntity<Page<Product>> getAllProducts(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                        @RequestParam(defaultValue = "8") Integer pageSize){
        Page<Product> productsList = productService.getAllProducts(pageNumber, pageSize);

        return ResponseEntity.ok(productsList);
    }

    @GetMapping("/items/category")
    public ResponseEntity<Page<Product>> getAllProductsFromCategory(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                                          @RequestParam(defaultValue = "9") Integer pageSize,
                                                                          @RequestParam long[] categoryId){
        Page<Product> productsList = productService.getAllProductsFromCategory(pageNumber, pageSize, categoryId);

        return ResponseEntity.ok(productsList);
    }
}
