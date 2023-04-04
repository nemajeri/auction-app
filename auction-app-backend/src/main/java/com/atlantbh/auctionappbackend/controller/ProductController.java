package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.dto.ProductDTO;
import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;
import com.atlantbh.auctionappbackend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/products")
public class ProductController {

    private final ProductService productService;


    @GetMapping(path = "/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(productService.getProductById(id));
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
            case "new-arrival":
                productDTOs = productService.getNewProducts(pageNumber, size);
                break;
            case "last-chance":
                productDTOs = productService.getLastProducts(pageNumber, size);
                break;
            default:
                return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(productDTOs);
    }

    @GetMapping("/all-products")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> productDTOs = productService.getAllProducts();
        return ResponseEntity.ok(productDTOs);
    }
}
