package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.dto.ProductDTO;
import com.atlantbh.auctionappbackend.mapper.ProductMapper;
import com.atlantbh.auctionappbackend.model.Product;
import com.atlantbh.auctionappbackend.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private ProductService underTest;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        underTest = new ProductService(productRepository, productMapper);
    }


    @Test
    void testGetNewProducts_ReturnsProductDTOList() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Shoes Collection", "New product description", 59.99f, List.of("./images/shoe-1.jpg"), true));
        products.add(new Product(2L, "Shoes Collection", "New product description", 39.99f, List.of("./images/shoe-1.jpg"), false));
        Mockito.when(productRepository.getNewArrivalsProducts(2, 0)).thenReturn(products);

        List<ProductDTO> expectedProductDTOs = new ArrayList<>();
        expectedProductDTOs.add(new ProductDTO(1L, "Shoes Collection", "New product description", 59.99f, List.of("./images/shoe-1.jpg"), true));
        expectedProductDTOs.add(new ProductDTO(2L, "Shoes Collection", "New product description", 39.99f, List.of("./images/shoe-1.jpg"), false));
        List<ProductDTO> productDTOList = underTest.getNewProducts(0, 2);

        assertEquals(expectedProductDTOs, productDTOList);
    }

    @Test
    void testGetLastProducts_ReturnsProductDTOList() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Shoes Collection", "New product description", 59.99f, List.of("./images/shoe-1.jpg"), true));
        products.add(new Product(2L, "Shoes Collection", "New product description", 39.99f, List.of("./images/shoe-1.jpg"), false));
        Mockito.when(productRepository.getLastChanceProducts(2, 0)).thenReturn(products);

        List<ProductDTO> expectedProductDTOs = new ArrayList<>();
        expectedProductDTOs.add(new ProductDTO(1L, "Shoes Collection", "New product description", 59.99f, List.of("./images/shoe-1.jpg"), true));
        expectedProductDTOs.add(new ProductDTO(2L, "Shoes Collection", "New product description", 39.99f, List.of("./images/shoe-1.jpg"), false));
        List<ProductDTO> productDTOList = underTest.getLastProducts(0, 2);

        assertEquals(expectedProductDTOs, productDTOList);
    }

    @Test
    void testGetAllProducts_ReturnsProductDTOList() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Shoes Collection", "New product description", 59.99f, List.of("./images/shoe-1.jpg"), true));
        products.add(new Product(2L, "Shoes Collection", "New product description", 39.99f, List.of("./images/shoe-1.jpg"), false));
        Mockito.when(productRepository.findAll()).thenReturn(products);

        List<ProductDTO> expectedProductDTOs = new ArrayList<>();
        expectedProductDTOs.add(new ProductDTO(1L, "Shoes Collection", "New product description", 59.99f, List.of("./images/shoe-1.jpg"), true));
        expectedProductDTOs.add(new ProductDTO(2L, "Shoes Collection", "New product description", 39.99f, List.of("./images/shoe-1.jpg"), false));
        List<ProductDTO> productDTOList = underTest.getAllProducts();

        assertEquals(expectedProductDTOs, productDTOList);
    }

    @Test
    void testGetAllProducts_ReturnsEmptyList_WhenNoProductsFound() {
        Mockito.when(productRepository.findAll()).thenReturn(new ArrayList<>());

        List<ProductDTO> productDTOList = underTest.getAllProducts();

        assertEquals(Collections.emptyList(), productDTOList);
    }

    @Test
    void testGetNewProducts_ReturnsEmptyList_WhenNoProductsFound() {
        Mockito.when(productRepository.getNewArrivalsProducts(2, 0)).thenReturn(new ArrayList<>());

        List<ProductDTO> productDTOList = underTest.getNewProducts(0, 2);

        assertEquals(Collections.emptyList(), productDTOList);
    }

    @Test
    void testGetLastProducts_ReturnsEmptyList_WhenNoProductsFound() {
        Mockito.when(productRepository.getLastChanceProducts(2, 0)).thenReturn(new ArrayList<>());

        List<ProductDTO> productDTOList = underTest.getLastProducts(0, 2);

        assertEquals(Collections.emptyList(), productDTOList);
    }
}