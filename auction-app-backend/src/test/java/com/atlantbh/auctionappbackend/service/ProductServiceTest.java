package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.dto.ProductDTO;
import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;
import com.atlantbh.auctionappbackend.mapper.ProductMapper;
import com.atlantbh.auctionappbackend.model.Product;
import com.atlantbh.auctionappbackend.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.math.BigInteger;
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
    @DisplayName("Test should return a product with the given Id")
    void testGetProductById_ReturnsProduct() throws ProductNotFoundException {
        Long id = 1L;
        Product product = new Product(id, "Shoes Collection", "New shoes collection", 10.00f, Collections.singletonList("/images/shoe-4.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), BigInteger.valueOf(5), BigDecimal.valueOf(25), true);
        ProductDTO expectedProductDTO = new ProductDTO(id, "Shoes Collection", "New shoes collection", 10.00f, Collections.singletonList("/images/shoe-4.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), BigInteger.valueOf(5), BigDecimal.valueOf(25), false);

        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(product));
        Mockito.when(productMapper.toProductDTO(product)).thenReturn(expectedProductDTO);

        ProductDTO actualProductDTO = underTest.getProductById(id);

        assertEquals(expectedProductDTO, actualProductDTO);
    }

    @Test
    void testGetNewProducts_ReturnsProductDTOList() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Shoes Collection", "New product description", 59.99f, List.of("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), BigInteger.valueOf(5), BigDecimal.valueOf(25), true));
        products.add(new Product(2L, "Shoes Collection", "New product description", 39.99f, List.of("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), BigInteger.valueOf(5), BigDecimal.valueOf(25), false));
        Mockito.when(productRepository.getNewArrivalsProducts(2, 0)).thenReturn(products);

        products.forEach(product -> Mockito.when(productMapper.toProductDTO(product)).thenReturn(createProductDTOFromProduct(product)));

        List<ProductDTO> expectedProductDTOs = new ArrayList<>();
        expectedProductDTOs.add(new ProductDTO(1L, "Shoes Collection", "New product description", 59.99f, List.of("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), BigInteger.valueOf(5), BigDecimal.valueOf(25), true));
        expectedProductDTOs.add(new ProductDTO(2L, "Shoes Collection", "New product description", 39.99f, List.of("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), BigInteger.valueOf(5), BigDecimal.valueOf(25), false));
        List<ProductDTO> productDTOList = underTest.getNewProducts(0, 2);

        assertEquals(expectedProductDTOs, productDTOList);
    }

    @Test
    void testGetLastProducts_ReturnsProductDTOList() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Shoes Collection", "New product description", 59.99f, List.of("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), BigInteger.valueOf(5), BigDecimal.valueOf(25), true));
        products.add(new Product(2L, "Shoes Collection", "New product description", 39.99f, List.of("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), BigInteger.valueOf(5), BigDecimal.valueOf(25), false));
        Mockito.when(productRepository.getLastChanceProducts(2, 0)).thenReturn(products);

        products.forEach(product -> Mockito.when(productMapper.toProductDTO(product)).thenReturn(createProductDTOFromProduct(product)));

        List<ProductDTO> expectedProductDTOs = new ArrayList<>();
        expectedProductDTOs.add(new ProductDTO(1L, "Shoes Collection", "New product description", 59.99f, List.of("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), BigInteger.valueOf(5), BigDecimal.valueOf(25), true));
        expectedProductDTOs.add(new ProductDTO(2L, "Shoes Collection", "New product description", 39.99f, List.of("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), BigInteger.valueOf(5), BigDecimal.valueOf(25), false));
        List<ProductDTO> productDTOList = underTest.getLastProducts(0, 2);

        assertEquals(expectedProductDTOs, productDTOList);
    }

    @Test
    void testGetAllProducts_ReturnsProductDTOList() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Shoes Collection", "New product description", 59.99f, List.of("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), BigInteger.valueOf(5), BigDecimal.valueOf(25), true));
        products.add(new Product(2L, "Shoes Collection", "New product description", 39.99f, List.of("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), BigInteger.valueOf(5), BigDecimal.valueOf(25), false));
        Mockito.when(productRepository.findAll()).thenReturn(products);

        products.forEach(product -> Mockito.when(productMapper.toProductDTO(product)).thenReturn(createProductDTOFromProduct(product)));

        List<ProductDTO> expectedProductDTOs = new ArrayList<>();
        expectedProductDTOs.add(new ProductDTO(1L, "Shoes Collection", "New product description", 59.99f, List.of("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), BigInteger.valueOf(5), BigDecimal.valueOf(25), true));
        expectedProductDTOs.add(new ProductDTO(2L, "Shoes Collection", "New product description", 39.99f, List.of("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), BigInteger.valueOf(5), BigDecimal.valueOf(25), false));
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

    private ProductDTO createProductDTOFromProduct(Product product) {
        return new ProductDTO(product.getId(), product.getProductName(), product.getDescription(), product.getStartPrice(), product.getImages(), product.getStartDate(), product.getEndDate(), product.getNumberOfBids(), product.getHighestBid(), product.isHighlighted());
    }
}
