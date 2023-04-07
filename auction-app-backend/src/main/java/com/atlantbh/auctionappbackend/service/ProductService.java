package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.dto.ProductDTO;
import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;
import com.atlantbh.auctionappbackend.mapper.ProductMapper;
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

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public Page<ProductsResponse> getAllProductsBySearchTerm(int pageNumber, int pageSize, String searchTerm) {
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

    public Page<ProductsResponse> getAllProductsFromCategory(int pageNumber, int pageSize, Long CategoryId) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> pagedResult = productRepository.findAllByCategoryId(CategoryId, pageable);

        Page<ProductsResponse> response = pagedResult.map(product -> new ProductsResponse(product.getId(), product.getProductName(), product.getStartPrice(),product.getImages().get(0), product.getCategory().getId()));

        return response;
    }

    public Page<ProductDTO> getNewProducts(int pageNumber, int size) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<Product> products = productRepository.getNewArrivalsProducts(pageable);
        return products.map(productMapper::toProductDTO);
    }


    public ProductDTO getProductById(Long id) throws ProductNotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return productMapper.toProductDTO(product);
    }

    public Page<ProductDTO> getLastProducts(int pageNumber, int size) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<Product> products = productRepository.getLastChanceProducts(pageable);
        return products.map(productMapper::toProductDTO);
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return mapToProductDTOList(products);
    }

    private List<ProductDTO> mapToProductDTOList(List<Product> products) {
        return products.stream()
                .map(productMapper::toProductDTO)
                .collect(Collectors.toList());
    }
}
