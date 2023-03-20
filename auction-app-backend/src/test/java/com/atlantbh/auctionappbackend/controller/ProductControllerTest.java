package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.dto.ProductDTO;
import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;
import com.atlantbh.auctionappbackend.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @Test
    public void testGetAllProducts_shouldReturnListOfProducts() {

        List<ProductDTO> expectedProducts = Arrays.asList(
                new ProductDTO(1L, "Shoes Collection 1", "New shoes collection", 10.00f, "./public/images/shoe-1.jpg"),
                new ProductDTO(2L, "Shoes Collection 2", "New shoes collection", 15.00f, "./public/images/shoe-2.jpg")
        );
        Mockito.when(productService.getAllProducts()).thenReturn(expectedProducts);


        ResponseEntity<List<ProductDTO>> actualResponse = productController.getAllProducts();


        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(expectedProducts);
    }

    @Test
    public void testGetProductById_shouldReturnProductWithGivenId_whenProductExists() {

        Long productId = 1L;
        ProductDTO expectedProduct = new ProductDTO(productId, "Shoes Collection", "New shoes collection", 10.00f, "./public/images/shoe-1.jpg");
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

