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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

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
    @DisplayName("Test should return a product with the given Id")
    void testGetProductById() throws ProductNotFoundException {
        Long id = 1L;
        Product product = new Product(id, "Shoes Collection", "New shoes collection", 10.00f, "/images/shoe-4.jpg,/images/shoe-5.jpg,/images/shoe-6.jpg,/images/shoe-7.jpg,/images/shoe-8.jpg", LocalDateTime.of(2023, 3, 23, 10, 0), LocalDateTime.of(2023, 3, 25, 12, 0), BigInteger.valueOf(5), BigInteger.valueOf(25));
        ProductDTO expectedProductDTO = new ProductDTO(id, "Shoes Collection", "New shoes collection", 10.00f, "/images/shoe-4.jpg,/images/shoe-5.jpg,/images/shoe-6.jpg,/images/shoe-7.jpg,/images/shoe-8.jpg", LocalDateTime.of(2023, 3, 23, 10, 0), LocalDateTime.of(2023, 3, 25, 12, 0), BigInteger.valueOf(5), BigInteger.valueOf(25));

        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(product));
        Mockito.when(productMapper.toProductDTO(product)).thenReturn(expectedProductDTO);

        ProductDTO actualProductDTO = underTest.getProductById(id);

        assertEquals(expectedProductDTO, actualProductDTO);
    }
}
