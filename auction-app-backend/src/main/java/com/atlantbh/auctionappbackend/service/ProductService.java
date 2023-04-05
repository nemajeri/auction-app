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

    public Page<ProductsResponse> getAllProductsBySearchTerm(Integer pageNumber, Integer pageSize, String searchTerm) {
        Page<Product> products;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        if (!searchTerm.isEmpty()) {
            products = productRepository.findByProductNameContainingIgnoreCase(searchTerm, pageable);
        } else {
            products = productRepository.findAll(pageable);
        }

        Page<ProductsResponse> productsResponse = products.map(product -> new ProductsResponse(product.getId(), product.getProductName(), product.getStartPrice(), product.getImages().get(0), product.getCategory().getId()));

        return productsResponse;
    }


    public Page<ProductsResponse> getAllProductsFromCategory(Integer pageNumber, Integer pageSize, Long CategoryId) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> pagedResult = productRepository.findAllByCategoryId(CategoryId, pageable);

        Page<ProductsResponse> response = pagedResult.map(product -> new ProductsResponse(product.getId(), product.getProductName(), product.getStartPrice(),product.getImages().get(0), product.getCategory().getId()));

        return response;
    }

}
