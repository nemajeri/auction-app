package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.enums.SortBy;
import com.atlantbh.auctionappbackend.exception.CategoryNotFoundException;
import com.atlantbh.auctionappbackend.model.Product;
import com.atlantbh.auctionappbackend.response.ProductsResponse;
import com.atlantbh.auctionappbackend.response.SingleProductResponse;
import com.atlantbh.auctionappbackend.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.atlantbh.auctionappbackend.dto.ProductDTO;
import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;


@RestController
@AllArgsConstructor
@RequestMapping("api/v1/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/search-suggestions")
    public ResponseEntity<String> searchSuggestions(@RequestParam("query") String query) {
        String bestSuggestion = productService.getSuggestion(query);
        return ResponseEntity.ok(bestSuggestion);
    }

    @GetMapping("/items")
    public ResponseEntity<Page<ProductsResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "9") int pageSize,
            @RequestParam(defaultValue = "") String searchTerm,
            @RequestParam(required = false) Long categoryId) {

        try {
            Page<ProductsResponse> productsList;
            productsList = productService.getAllFilteredProducts(pageNumber, pageSize, searchTerm, categoryId);
            return ResponseEntity.ok(productsList);
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/items/app-user")
    public ResponseEntity<List<Product>> retrieveProductsFromUser(@RequestParam Long userId,
                                                                  @RequestParam String type){
        SortBy sortBy = SortBy.fromValue(type);
        List<Product> products = productService.retrieveUserProductsByType(userId, sortBy);

        return new ResponseEntity<>(products, OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<SingleProductResponse> getProductById(@PathVariable("id") Long id, HttpServletRequest request) {
        try {
            return ResponseEntity.ok(productService.getProductById(id, request));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/filtered-products")
    public ResponseEntity<Page<ProductDTO>> getFilteredProducts(
            @RequestParam String filter,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "8") int size) {
        Page<ProductDTO> productDTOs;
        switch (filter) {
            case "new-arrival" -> productDTOs = productService.getNewProducts(pageNumber, size);
            case "last-chance" -> productDTOs = productService.getLastProducts(pageNumber, size);
            default -> {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.ok().body(productDTOs);
    }

    @GetMapping("/all-products")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> productDTOs = productService.getAllProducts();
        return ResponseEntity.ok(productDTOs);
    }
}
