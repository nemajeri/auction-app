package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.dto.ProductDTO;
import com.atlantbh.auctionappbackend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController()
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/sorted-&-paginated-products")
    public ResponseEntity<List<ProductDTO>> getProducts(
            @RequestParam(required = false) String filter,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "8") int size) {
        switch (filter) {
            case "new-arrival":
                return ResponseEntity.ok(productService.getNewProducts(pageNumber, size));
            case "last-chance":
                return ResponseEntity.ok(productService.getLastProducts(pageNumber, size));
            case "all":
                return ResponseEntity.ok(productService.getAllProducts());
            default:
                return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }
}
