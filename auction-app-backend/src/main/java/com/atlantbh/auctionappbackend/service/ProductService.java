package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.model.Product;
import com.atlantbh.auctionappbackend.repository.ProductRepository;
import com.atlantbh.auctionappbackend.response.ProductsResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductService {
    private ProductRepository productRepository;

    public Page<ProductsResponse> getAllProducts(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Product> pagedResult = productRepository.findAll(pageable);

        Page<ProductsResponse> response = pagedResult.map(product -> new ProductsResponse(product.getId(), product.getProductName(), product.getStartPrice(), product.getImages() ,product.getCategory().getId()));

        return response;
    }



    public Page<ProductsResponse> getAllProductsFromCategory(Integer pageNumber, Integer pageSize, Long CategoryId, String searchTerm) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> pagedResult = productRepository.findAllByCategory_IdAndProductNameContaining(CategoryId, pageable, searchTerm);

        Page<ProductsResponse> response = pagedResult.map(product -> new ProductsResponse(product.getId(), product.getProductName(), product.getStartPrice(),product.getImages(), product.getCategory().getId()));

        return response;
    }
}
