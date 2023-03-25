package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.model.Product;
import com.atlantbh.auctionappbackend.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductService {
    private ProductRepository productRepository;

    public Page<Product> getAllProducts(Integer pageNumber, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNumber, pageSize);

        Page<Product> pagedResult = productRepository.findAll(paging);

        return pagedResult;
    }


    public Page<Product> getAllProductsFromCategory(Integer pageNumber, Integer pageSize,long[] categoryId){
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        Page<Product> productsList = productRepository.findAllByCategoryIdIn(categoryId, paging);

        return productsList;
    }
}
