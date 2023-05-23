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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;


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
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String sort) {

        try {
            Page<ProductsResponse> productsList;
            productsList = productService.getAllFilteredProducts(pageNumber, pageSize, searchTerm, categoryId, SortBy.fromName(sort));
            return ResponseEntity.ok(productsList);
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/recommended")
    public ResponseEntity<List<ProductsResponse>> getRecommendedProducts(@RequestParam("userId") Long userId) {
        List<ProductsResponse> recommendedProducts = productService.getRecommendedProducts(userId);
        return new ResponseEntity<>(recommendedProducts, HttpStatus.OK);
    }


    @GetMapping("/items/app-user")
    public ResponseEntity<List<Product>> retrieveProductsFromUser(@RequestParam Long userId,
                                                                  @RequestParam String type){
        SortBy sortBy = SortBy.fromValue(type);
        List<Product> products = productService.retrieveUserProductsByType(userId, sortBy);

        return new ResponseEntity<>(products, OK);
    }

    @PostMapping(path = "/add-item", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> addNewItem(@RequestPart("productDetails") @Valid NewProductRequest request,
                                        @RequestPart("images") List<MultipartFile> images,
                                        BindingResult bindingResult,
                                        HttpServletRequest httpServletRequest) {
        Map<String, String> response = new HashMap<>();
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(BAD_REQUEST).body(errors);
        }

        try {
            productService.createProduct(request, images, httpServletRequest);
            response.put("status", "created");
            return new ResponseEntity<>(CREATED);
        } catch (Exception e) {
            response.clear();
            response.put("error: ", "Error creating product: " + e.getMessage());
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }


    @GetMapping(path = "/{productId}")
    public ResponseEntity<SingleProductResponse> getProductById(@PathVariable("productId") Long productId) {
        try {
            return ResponseEntity.ok(productService.getProductById(productId));
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
