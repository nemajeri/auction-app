package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.dto.ProductDTO;
import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;
import com.atlantbh.auctionappbackend.mapper.ProductMapper;
import com.atlantbh.auctionappbackend.model.Product;
import com.atlantbh.auctionappbackend.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<ProductDTO> getNewProducts(int pageNumber, int size) {
        int offset = pageNumber * size;
        List<Product> products = productRepository.getNewArrivalsProducts(size, offset);
        if (products.isEmpty()) {
            return Collections.emptyList();
        }
        return mapToProductDTOList(products);
    }

    public ProductDTO getProductById(Long id) throws ProductNotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (!optionalProduct.isPresent()) {
            throw new ProductNotFoundException(id);
        }

        Product product = optionalProduct.get();
        ProductDTO productDTO = productMapper.toProductDTO(product);

        return productDTO;
    }

    public List<ProductDTO> getLastProducts(int pageNumber, int size) {
        int offset = pageNumber * size;
        List<Product> products = productRepository.getLastChanceProducts(size, offset);
        if (products.isEmpty()) {
            return Collections.emptyList();
        }
        return mapToProductDTOList(products);
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            return Collections.emptyList();
        }
        return mapToProductDTOList(products);
    }

    private List<ProductDTO> mapToProductDTOList(List<Product> products) {
        return products.stream()
                .map(productMapper::toProductDTO)
                .collect(Collectors.toList());
    }
}
