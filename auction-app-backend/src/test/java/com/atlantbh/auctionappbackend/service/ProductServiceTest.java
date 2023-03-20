package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.dto.ProductDTO;
import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;
import com.atlantbh.auctionappbackend.mapper.ProductMapper;
import com.atlantbh.auctionappbackend.model.Product;
import com.atlantbh.auctionappbackend.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private ProductService underTest;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        underTest = new ProductService(productRepository, productMapper);
    }

    @Test
    @DisplayName("Test should return product data transfer objects")
    void testGetAllProducts_ReturnsProductDTOList() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Shoes Collection", "New product description", 59.99f, "./public/images/shoe-1.jpg"));
        products.add(new Product(2L, "Shoes Collection", "New product description", 39.99f, "./public/images/shoe-1.jpg"));
        Mockito.when(productRepository.findAll()).thenReturn(products);

        List<ProductDTO> expectedProductDTOs = new ArrayList<>();
        expectedProductDTOs.add(new ProductDTO(1L, "Shoes Collection", "New product description", 59.99f, "./public/images/shoe-1.jpg"));
        expectedProductDTOs.add(new ProductDTO(2L, "Shoes Collection", "New product description", 39.99f, "./public/images/shoe-1.jpg"));
        List<ProductDTO> productDTOList = underTest.getAllProducts();

        assertEquals(expectedProductDTOs, productDTOList);
    }

    @Test
    @DisplayName("Test should return empty list")
    void testGetAllProducts_ReturnsEmptyList_WhenNoProductsFound() {
        Mockito.when(productRepository.findAll()).thenReturn(new ArrayList<>());

        List<ProductDTO> productDTOList = underTest.getAllProducts();

        assertEquals(Collections.emptyList(), productDTOList);
    }

    @Test
    @DisplayName("Test should return a product with the given Id")
    void testGetProductById() throws ProductNotFoundException {
        Long id = 1L;
        Product product = new Product(id, "Shoes Collection", "New product description", 59.99f, "./public/images/shoe-1.jpg");
        ProductDTO expectedProductDTO = new ProductDTO(id, "Shoes Collection", "New product description", 59.99f, "./public/images/shoe-1.jpg");

        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(product));
        Mockito.when(productMapper.toProductDTO(product)).thenReturn(expectedProductDTO);

        ProductDTO actualProductDTO = underTest.getProductById(id);

        assertEquals(expectedProductDTO, actualProductDTO);
    }
}