package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.dto.ProductDTO;
import com.atlantbh.auctionappbackend.enums.SortBy;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.atlantbh.auctionappbackend.utils.LevenshteinDistanceCalculation.calculate;


@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;


    public String getSuggestion(String query) {
        List<String> suggestions = productRepository.findTopNamesByNameSimilarity(query);

        if (suggestions.isEmpty()) {
            return null;
        } else {
            String bestSuggestion = null;
            int bestWeight = Integer.MAX_VALUE;

            for (String suggestion : suggestions) {
                int weight = calculate(query.toLowerCase(), suggestion.toLowerCase());
                if (weight < bestWeight) {
                    bestSuggestion = suggestion;
                    bestWeight = weight;
                }
            }

            return bestSuggestion;
        }
    }

    public List<Product> retrieveUserProductsByType(Long userId, SortBy sortingType) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Product> userProducts;

        if (sortingType == SortBy.SOLD) {
            userProducts = productRepository.findAllProductsByUserIdAndEndDateIsBefore(
                    userId, currentTime, Sort.by(Sort.Direction.DESC, SortBy.END_DATE.getSort()));
        } else {
            userProducts = productRepository.findAllProductsByUserIdAndEndDateIsAfter(
                    userId, currentTime, Sort.by(Sort.Direction.DESC, SortBy.START_DATE.getSort()));
        }

        return userProducts;
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
