package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.dto.ProductDTO;
import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;
import com.atlantbh.auctionappbackend.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.math.BigInteger;
import java.time.LocalDateTime;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;


    @Test
    public void testGetProductById_shouldReturnProductWithGivenId_whenProductExists() {

        Long productId = 1L;
        ProductDTO expectedProduct = new ProductDTO(productId, "Shoes Collection", "New shoes collection", 10.00f, "/images/shoe-4.jpg,/images/shoe-5.jpg,/images/shoe-6.jpg,/images/shoe-7.jpg,/images/shoe-8.jpg", LocalDateTime.of(2023, 3, 23, 10, 0), LocalDateTime.of(2023, 3, 25, 12, 0), BigInteger.valueOf(5), BigInteger.valueOf(25));
        Mockito.when(productService.getProductById(productId)).thenReturn(expectedProduct);


        ResponseEntity<ProductDTO> actualResponse = productController.getProductById(productId);

        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(expectedProduct);
    }

    @Test
    public void testGetProductById_shouldReturn404_whenProductDoesNotExist() {

        Long productId = 1L;
        Mockito.when(productService.getProductById(productId)).thenThrow(new ProductNotFoundException(productId));


        ResponseEntity<ProductDTO> actualResponse = productController.getProductById(productId);


        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(actualResponse.getBody()).isNull();
    }
}
