package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.dto.ProductDTO;
import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;
import com.atlantbh.auctionappbackend.mapper.ProductMapper;
import com.atlantbh.auctionappbackend.model.Product;
import com.atlantbh.auctionappbackend.repository.ProductRepository;
import com.atlantbh.auctionappbackend.response.ProductsResponse;
import com.atlantbh.auctionappbackend.utils.ProductSpecifications;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.atlantbh.auctionappbackend.utils.LevenshteinDistance.calculateDistance;


@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;


    public List<String> getSuggestion(String query) {
        int maxDistance = 10;
        List<String> suggestions = productRepository.findTopNamesByNameSimilarity(query, maxDistance);
        return suggestions;
    }


    public Page<ProductsResponse> getAllFilteredProducts(int pageNumber, int pageSize, String searchTerm, Long categoryId) {
        Specification<Product> specification = Specification.where(ProductSpecifications.hasNameLike(searchTerm));

        if (categoryId != null) {
            specification = specification.and(ProductSpecifications.hasCategoryId(categoryId));
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> products = productRepository.findAll(specification, pageable);
        return products.map(product -> new ProductsResponse(product.getId(), product.getProductName(), product.getStartPrice(), product.getImages().get(0), product.getCategory().getId()));
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
