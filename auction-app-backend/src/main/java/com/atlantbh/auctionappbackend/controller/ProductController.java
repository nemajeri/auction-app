package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.enums.SortBy;
import com.atlantbh.auctionappbackend.exception.CategoryNotFoundException;
import com.atlantbh.auctionappbackend.model.Product;
import com.atlantbh.auctionappbackend.request.NewProductRequest;
import com.atlantbh.auctionappbackend.response.ProductsResponse;
import com.atlantbh.auctionappbackend.response.SingleProductResponse;
import com.atlantbh.auctionappbackend.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/products")
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

    @PostMapping(path = "/add-item")
    public ResponseEntity<?> addNewItem(@Valid @RequestBody NewProductRequest request, BindingResult bindingResult, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        try {
            Product savedProduct = productService.createProduct(request, httpServletRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating product: " + e.getMessage());
        }
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
    public ResponseEntity<Page<ProductsResponse>> getFilteredProducts(
            @RequestParam String filter,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "8") int size) {
        Page<ProductsResponse> productDTOs;
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
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> productDTOs = productService.getAllProducts();
        return ResponseEntity.ok(productDTOs);
    }
}
