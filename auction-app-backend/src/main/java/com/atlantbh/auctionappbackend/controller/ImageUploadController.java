package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.service.ProductService;
import com.atlantbh.auctionappbackend.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.atlantbh.auctionappbackend.utils.Constants.S3_KEY_PREFIX;

@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
public class ImageUploadController {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ProductService productService;

    @PostMapping("/{productId}")
    public ResponseEntity<String> uploadImage(@PathVariable Long productId, @RequestParam("file") MultipartFile file) {
        String url = s3Service.uploadFile(file, S3_KEY_PREFIX + productId + "/");
        productService.addProductImage(productId, url);
        return ResponseEntity.ok(url);
    }
}
