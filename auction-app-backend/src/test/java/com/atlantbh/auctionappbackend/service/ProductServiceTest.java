package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.dto.ProductDTO;
import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;
import com.atlantbh.auctionappbackend.mapper.ProductMapper;
import com.atlantbh.auctionappbackend.model.Product;
import com.atlantbh.auctionappbackend.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


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
    @DisplayName("Test should return new arrival products")
    void testGetNewProducts_ReturnsProductDTOList() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Shoes Collection", "New product description", 59.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), BigInteger.valueOf(5), BigDecimal.valueOf(25), true));
        products.add(new Product(2L, "Shoes Collection", "New product description", 39.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), BigInteger.valueOf(5), BigDecimal.valueOf(25), false));

        int pageNumber = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(pageNumber, size);
        Mockito.when(productRepository.getNewArrivalsProducts(pageable)).thenReturn(new PageImpl<>(products));

        products.forEach(product -> Mockito.when(productMapper.toProductDTO(product)).thenReturn(createProductDTOFromProduct(product)));

        List<ProductDTO> expectedProductDTOs = new ArrayList<>();
        expectedProductDTOs.add(new ProductDTO(1L, "Shoes Collection", "New product description", 59.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), BigInteger.valueOf(5), BigDecimal.valueOf(25), true));
        expectedProductDTOs.add(new ProductDTO(2L, "Shoes Collection", "New product description", 39.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), BigInteger.valueOf(5), BigDecimal.valueOf(25), false));

        Page<ProductDTO> productDTOPage = underTest.getNewProducts(pageNumber, size);
        List<ProductDTO> actualProductDTOs = productDTOPage.getContent();

        assertEquals(expectedProductDTOs, actualProductDTOs);
    }

    @Test
    @DisplayName("Test should return last chance products")
    void testGetLastProducts_ReturnsProductDTOList() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Shoes Collection", "New product description", 59.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), BigInteger.valueOf(5), BigDecimal.valueOf(25), true));
        products.add(new Product(2L, "Shoes Collection", "New product description", 39.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), BigInteger.valueOf(5), BigDecimal.valueOf(25), false));

        int pageNumber = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(pageNumber, size);
        Mockito.when(productRepository.getLastChanceProducts(pageable)).thenReturn(new PageImpl<>(products));

        products.forEach(product -> Mockito.when(productMapper.toProductDTO(product)).thenReturn(createProductDTOFromProduct(product)));

        List<ProductDTO> expectedProductDTOs = new ArrayList<>();
        expectedProductDTOs.add(new ProductDTO(1L, "Shoes Collection", "New product description", 59.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), BigInteger.valueOf(5), BigDecimal.valueOf(25), true));
        expectedProductDTOs.add(new ProductDTO(2L, "Shoes Collection", "New product description", 39.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), BigInteger.valueOf(5), BigDecimal.valueOf(25), false));

        Page<ProductDTO> productDTOPage = underTest.getLastProducts(pageNumber, size);
        List<ProductDTO> productDTOList = productDTOPage.getContent();

        assertEquals(expectedProductDTOs, productDTOList);
    }

    @Test
    @DisplayName("Test should return all products")
    void testGetAllProducts_ReturnsProductDTOList() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Shoes Collection", "New product description", 59.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), BigInteger.valueOf(5), BigDecimal.valueOf(25), true));
        products.add(new Product(2L, "Shoes Collection", "New product description", 39.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), BigInteger.valueOf(5), BigDecimal.valueOf(25), false));
        Mockito.when(productRepository.findAll()).thenReturn(products);

        products.forEach(product -> Mockito.when(productMapper.toProductDTO(product)).thenReturn(createProductDTOFromProduct(product)));

        List<ProductDTO> expectedProductDTOs = new ArrayList<>();
        expectedProductDTOs.add(new ProductDTO(1L, "Shoes Collection", "New product description", 59.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), BigInteger.valueOf(5), BigDecimal.valueOf(25), true));
        expectedProductDTOs.add(new ProductDTO(2L, "Shoes Collection", "New product description", 39.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), BigInteger.valueOf(5), BigDecimal.valueOf(25), false));
        List<ProductDTO> productDTOList = underTest.getAllProducts();

        assertEquals(expectedProductDTOs, productDTOList);
    }

    @Test
    @DisplayName("Test should return empty list when products not found")
    void testGetAllProducts_ReturnsEmptyList_WhenNoProductsFound() {
        Mockito.when(productRepository.findAll()).thenReturn(new ArrayList<>());

        List<ProductDTO> productDTOList = underTest.getAllProducts();

        assertEquals(Collections.emptyList(), productDTOList);
    }

    @Test
    @DisplayName("Test should return empty list when products not found")
    void testGetNewProducts_ReturnsEmptyList_WhenNoProductsFound() {
        int pageNumber = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(pageNumber, size);
        Mockito.when(productRepository.getNewArrivalsProducts(pageable)).thenReturn(Page.empty());

        Page<ProductDTO> productDTOPage = underTest.getNewProducts(pageNumber, size);
        List<ProductDTO> actualProductDTOs = productDTOPage.getContent();

        assertEquals(Collections.emptyList(), actualProductDTOs);
    }

    @Test
    @DisplayName("Test should return empty list when products not found")
    void testGetLastProducts_ReturnsEmptyList_WhenNoProductsFound() {
        int pageNumber = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(pageNumber, size);
        Mockito.when(productRepository.getLastChanceProducts(pageable)).thenReturn(Page.empty());

        Page<ProductDTO> productDTOList = underTest.getLastProducts(pageNumber, size);
        List<ProductDTO> productDTOs = productDTOList.getContent();

        assertEquals(Collections.emptyList(), productDTOs);
    }

    private ProductDTO createProductDTOFromProduct(Product product) {
        return new ProductDTO(product.getId(), product.getProductName(), product.getDescription(), product.getStartPrice(), product.getImages(), product.getStartDate(), product.getEndDate(), product.getNumberOfBids(), product.getHighestBid(), product.isHighlighted());
    }
}
