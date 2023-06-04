package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.enums.SortBy;
import com.atlantbh.auctionappbackend.exception.CategoryNotFoundException;
import com.atlantbh.auctionappbackend.exception.UnprocessableCSVFileException;
import com.atlantbh.auctionappbackend.request.NewProductRequest;
import com.atlantbh.auctionappbackend.response.AppUserProductsResponse;
import com.atlantbh.auctionappbackend.response.HighlightedProductResponse;
import com.atlantbh.auctionappbackend.response.ProductsResponse;
import com.atlantbh.auctionappbackend.response.SingleProductResponse;
import com.atlantbh.auctionappbackend.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import static org.springframework.http.HttpStatus.*;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

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
    public ResponseEntity<List<AppUserProductsResponse>> retrieveProductsFromUser(@RequestParam Long userId,
                                                                                  @RequestParam String type){
        SortBy sortBy = SortBy.fromValue(type);
        List<AppUserProductsResponse> products = productService.retrieveUserProductsByType(userId, sortBy);
        return new ResponseEntity<>(products, OK);
    }

    @PostMapping(path = "/add-item", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addNewItem(@RequestPart("productDetails") @Valid NewProductRequest request,
                                           @RequestPart("images") List<MultipartFile> images,
                                           BindingResult bindingResult,
                                           HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(BAD_REQUEST).build();
        }

        try {
            productService.createProduct(request, images, httpServletRequest);
            return new ResponseEntity<>(CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    @PostMapping("/upload-csv-file")
    public ResponseEntity<Void> uploadCSVFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } else {
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                productService.processCsvFileToCreateProduct(reader);
                return new ResponseEntity<>(HttpStatus.CREATED);
            } catch (Exception ex) {
                log.error("Error processing CSV file: ", ex);
                throw new UnprocessableCSVFileException("Error processing the uploaded CSV file. Please ensure the file is in the correct format and try again.");
            }
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

    @GetMapping("/highlighted-products")
    public ResponseEntity<List<HighlightedProductResponse>> getHighlightedProducts() {
        List<HighlightedProductResponse> productResponse = productService.getHighlightedProducts();
        return ResponseEntity.ok(productResponse);
    }
}
