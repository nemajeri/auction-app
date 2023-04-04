package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.model.Product;
import com.atlantbh.auctionappbackend.repository.ProductRepository;
import com.atlantbh.auctionappbackend.response.ProductsResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {
    private ProductRepository productRepository;

    public List<ProductsResponse> getAllProducts(String searchTerm) {
        List<Product> productList;

        if (!searchTerm.isEmpty()) {
            productList = productRepository.findByProductNameContainingIgnoreCase(searchTerm);
        } else {
            productList = productRepository.findAll();
        }

        List<ProductsResponse> productsListResponse = productList.stream().map(product -> new ProductsResponse(product.getId(), product.getProductName(), product.getStartPrice(), product.getImages().get(0), product.getCategory().getId())).collect(Collectors.toList());

        return productsListResponse;
    }


    public Page<ProductsResponse> getAllProductsFromCategory(Integer pageNumber, Integer pageSize, Long CategoryId) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> pagedResult = productRepository.findAllByCategoryId(CategoryId, pageable);

        Page<ProductsResponse> response = pagedResult.map(product -> new ProductsResponse(product.getId(), product.getProductName(), product.getStartPrice(),product.getImages().get(0), product.getCategory().getId()));

        return response;
    }

}
