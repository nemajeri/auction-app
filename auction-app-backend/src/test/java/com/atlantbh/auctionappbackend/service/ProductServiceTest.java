package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.dto.ProductDTO;
import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;
import com.atlantbh.auctionappbackend.mapper.ProductMapper;
import com.atlantbh.auctionappbackend.model.Category;
import com.atlantbh.auctionappbackend.model.Product;
import com.atlantbh.auctionappbackend.repository.ProductRepository;
import com.atlantbh.auctionappbackend.response.ProductsResponse;
import com.atlantbh.auctionappbackend.utils.ProductSpecifications;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService underTest;

    @Test
    @DisplayName("Test should return filtered products")
    void testGetAllFilteredProducts() {
        List<Product> products = new ArrayList<>();
        Category category1 = new Category(1L, "Women");

        products.add(new Product(7L, "Example Product 6", "A example product", 79.99f, Collections.singletonList("/images/shoe-4.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, false, category1));
        products.add(new Product(9L, "Example Product 8", "A example product", 99.99f, Collections.singletonList("/images/shoe-2.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, false, category1));

        int pageNumber = 0;
        int pageSize = 9;
        String searchTerm = "";
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Mockito.when(productRepository.findAll(ArgumentMatchers.<Specification<Product>>any(), ArgumentMatchers.<Pageable>any())).thenReturn(new PageImpl<>(products, pageable, 1));

        List<ProductsResponse> expectedProductsResponses = new ArrayList<>();
        expectedProductsResponses.add(new ProductsResponse(7L, "Example Product 6", 79.99f, "/images/shoe-4.jpg", 1L));
        expectedProductsResponses.add(new ProductsResponse(9L, "Example Product 8", 99.99f, "/images/shoe-2.jpg", 1L));

        Page<ProductsResponse> productsResponsePage = underTest.getAllFilteredProducts(pageNumber, pageSize, searchTerm, categoryId);
        List<ProductsResponse> actualProductsResponses = productsResponsePage.getContent();

        assertEquals(expectedProductsResponses, actualProductsResponses);
    }


    @Test
    @DisplayName("Test should return a product with the given Id")
    void testGetProductById_ReturnsProduct() throws ProductNotFoundException {
        Long id = 1L;

        Category category2 = new Category(2L, "Men");

        Product product = new Product(id, "Shoes Collection", "New shoes collection", 10.00f, Collections.singletonList("/images/shoe-4.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f,true ,category2);
        ProductDTO expectedProductDTO = new ProductDTO(id, "Shoes Collection", "New shoes collection", 10.00f, Collections.singletonList("/images/shoe-4.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25,2L, "Men" ,false);

        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(product));
        Mockito.when(productMapper.toProductDTO(product)).thenReturn(expectedProductDTO);

        ProductDTO actualProductDTO = underTest.getProductById(id);

        assertEquals(expectedProductDTO, actualProductDTO);
    }

    @Test
    @DisplayName("Test should return new arrival products")
    void testGetNewProducts_ReturnsProductDTOList() {
        List<Product> products = new ArrayList<>();

        Category category1 = new Category(1L, "Women");

        products.add(new Product(1L, "Shoes Collection", "New product description", 59.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f,false, category1));
        products.add(new Product(2L, "Shoes Collection", "New product description", 59.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f,false, category1));


        int pageNumber = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(pageNumber, size);
        Mockito.when(productRepository.getNewArrivalsProducts(pageable)).thenReturn(new PageImpl<>(products));

        products.forEach(product -> Mockito.when(productMapper.toProductDTO(product)).thenReturn(createProductDTOFromProduct(product)));

        List<ProductDTO> expectedProductDTOs = new ArrayList<>();
        expectedProductDTOs.add(new ProductDTO(1L, "Shoes Collection", "New product description", 59.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, 1L, "Women", false));
        expectedProductDTOs.add(new ProductDTO(2L, "Shoes Collection", "New product description", 59.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, 1L, "Women", false));

        Page<ProductDTO> productDTOPage = underTest.getNewProducts(pageNumber, size);
        List<ProductDTO> actualProductDTOs = productDTOPage.getContent();

        assertEquals(expectedProductDTOs, actualProductDTOs);
    }

    @Test
    @DisplayName("Test should return last chance products")
    void testGetLastProducts_ReturnsProductDTOList() {
        List<Product> products = new ArrayList<>();

        Category category2 = new Category(2L, "Men");

        products.add(new Product(1L, "Shoes Collection", "New product description", 59.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, true, category2));
        products.add(new Product(2L, "Shoes Collection", "New product description", 39.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, false, category2));

        int pageNumber = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(pageNumber, size);
        Mockito.when(productRepository.getLastChanceProducts(pageable)).thenReturn(new PageImpl<>(products));

        products.forEach(product -> Mockito.when(productMapper.toProductDTO(product)).thenReturn(createProductDTOFromProduct(product)));

        List<ProductDTO> expectedProductDTOs = new ArrayList<>();
        expectedProductDTOs.add(new ProductDTO(1L, "Shoes Collection", "New product description", 59.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f,2L, "Men", true));
        expectedProductDTOs.add(new ProductDTO(2L, "Shoes Collection", "New product description", 39.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f,2L, "Men", false));

        Page<ProductDTO> productDTOPage = underTest.getLastProducts(pageNumber, size);
        List<ProductDTO> productDTOList = productDTOPage.getContent();

        assertEquals(expectedProductDTOs, productDTOList);
    }

    @Test
    @DisplayName("Test should return all products")
    void testGetAllProducts_ReturnsProductDTOList() {
        List<Product> products = new ArrayList<>();

        Category category1 = new Category(1L, "Women");

        products.add(new Product(1L, "Shoes Collection", "New product description", 59.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0),5,35.00f,true, category1));
        products.add(new Product(2L, "Shoes Collection", "New product description", 39.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, false, category1));
        Mockito.when(productRepository.findAll()).thenReturn(products);

        products.forEach(product -> Mockito.when(productMapper.toProductDTO(product)).thenReturn(createProductDTOFromProduct(product)));

        List<ProductDTO> expectedProductDTOs = new ArrayList<>();
        expectedProductDTOs.add(new ProductDTO(1L, "Shoes Collection", "New product description", 59.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 35.00f, 1L, "Women",true));
        expectedProductDTOs.add(new ProductDTO(2L, "Shoes Collection", "New product description", 39.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f,1L, "Women" ,false));
        List<ProductDTO> productDTOList = underTest.getAllProducts();

        assertEquals(expectedProductDTOs, productDTOList);
    }


    private ProductDTO createProductDTOFromProduct(Product product) {
        return new ProductDTO(product.getId(), product.getProductName(), product.getDescription(), product.getStartPrice(), product.getImages(), product.getStartDate(), product.getEndDate(), product.getNumberOfBids(), product.getHighestBid(), product.getCategory().getId(),product.getCategory().getCategoryName(), product.isHighlighted());
    }
}
