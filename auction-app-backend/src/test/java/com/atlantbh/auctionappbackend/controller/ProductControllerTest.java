package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.dto.ProductDTO;
import com.atlantbh.auctionappbackend.security.JwtAuthenticationEntryPoint;
import com.atlantbh.auctionappbackend.security.JwtTokenProvider;
import com.atlantbh.auctionappbackend.service.ProductService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    public void testGetProductById_ReturnsAProductById() throws Exception {

        ProductDTO product = new ProductDTO(
                1L,
                "Product Name",
                "Product Description",
                10.0f,
                Arrays.asList("image1.jpg", "image2.jpg"),
                LocalDateTime.of(2023, 3, 23, 0, 0),
                LocalDateTime.of(2023, 4, 15, 0, 0),
                5,
                10.00f,
                1L,
                "Men",
                true
        );

        when(productService.getProductById(product.getId())).thenReturn(product);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");


        mockMvc.perform(get("/api/v1/products/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.productName").value(product.getProductName()))
                .andExpect(jsonPath("$.description").value(product.getDescription()))
                .andExpect(jsonPath("$.startPrice").value(product.getStartPrice()))
                .andExpect(jsonPath("$.images").isArray())
                .andExpect(jsonPath("$.images[0]").value(product.getImages().get(0)))
                .andExpect(jsonPath("$.images[1]").value(product.getImages().get(1)))
                .andExpect(jsonPath("$.startDate").value(product.getStartDate().format(formatter)))
                .andExpect(jsonPath("$.endDate").value(product.getEndDate().format(formatter)))
                .andExpect(jsonPath("$.numberOfBids").value(product.getNumberOfBids()))
                .andExpect(jsonPath("$.highestBid").value(product.getHighestBid()))
                .andReturn();
    }
    @Test
    public void testGetProductsNewArrivals_ReturnsNewestProducts() throws Exception {

        ProductDTO product1 = new ProductDTO(7L, "Example Product 6", "A example product", 79.99f, Collections.singletonList("/images/shoe-4.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, 1L,"Women",false);
        ProductDTO product2 = new ProductDTO(9L, "Example Product 8", "A example product", 99.99f, Collections.singletonList("/images/shoe-2.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, 1L,"Women",false);

        int pageNumber = 0;
        int size = 8;
        Pageable pageable = PageRequest.of(pageNumber, size);
        when(productService.getNewProducts(pageNumber, size))
                .thenReturn(new PageImpl<>(Arrays.asList(product1, product2), pageable, 1));

        mockMvc.perform(get("/api/v1/products/filtered-products")
                        .param("filter", "new-arrival")
                        .param("pageNumber", String.valueOf(pageNumber))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(product1.getId()))
                .andExpect(jsonPath("$.content[0].productName").value(product1.getProductName()))
                .andExpect(jsonPath("$.content[0].description").value(product1.getDescription()))
                .andExpect(jsonPath("$.content[0].startPrice").value(product1.getStartPrice()))
                .andExpect(jsonPath("$.content[0].images[0]").value(product1.getImages().get(0)))
                .andExpect(jsonPath("$.content[0].highlighted").value(product1.isHighlighted()))
                .andExpect(jsonPath("$.content[1].id").value(product2.getId()))
                .andExpect(jsonPath("$.content[1].productName").value(product2.getProductName()))
                .andExpect(jsonPath("$.content[1].description").value(product2.getDescription()))
                .andExpect(jsonPath("$.content[1].startPrice").value(product2.getStartPrice()))
                .andExpect(jsonPath("$.content[1].images[0]").value(product2.getImages().get(0)))
                .andExpect(jsonPath("$.content[1].highlighted").value(product1.isHighlighted()));
    }

    @Test
    public void testGetProductsLastChance_ReturnsLastChanceProducts() throws Exception {

        ProductDTO product1 = new ProductDTO(7L, "Example Product 6", "A example product", 79.99f, Collections.singletonList("/images/shoe-4.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, 1L,"Men",false);
        ProductDTO product2 = new ProductDTO(9L, "Example Product 8", "A example product", 99.99f, Collections.singletonList("/images/shoe-2.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, 1L,"Women",false);

        int pageNumber = 0;
        int size = 8;
        Pageable pageable = PageRequest.of(pageNumber, size);
        when(productService.getLastProducts(pageNumber, size))
                .thenReturn(new PageImpl<>(Arrays.asList(product1, product2), pageable, 1));

        mockMvc.perform(get("/api/v1/products/filtered-products")
                        .param("filter", "last-chance")
                        .param("pageNumber", String.valueOf(pageNumber))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(product1.getId()))
                .andExpect(jsonPath("$.content[0].productName").value(product1.getProductName()))
                .andExpect(jsonPath("$.content[0].description").value(product1.getDescription()))
                .andExpect(jsonPath("$.content[0].startPrice").value(product1.getStartPrice()))
                .andExpect(jsonPath("$.content[0].images[0]").value(product1.getImages().get(0)))
                .andExpect(jsonPath("$.content[0].highlighted").value(product1.isHighlighted()))
                .andExpect(jsonPath("$.content[1].id").value(product2.getId()))
                .andExpect(jsonPath("$.content[1].productName").value(product2.getProductName()))
                .andExpect(jsonPath("$.content[1].description").value(product2.getDescription()))
                .andExpect(jsonPath("$.content[1].startPrice").value(product2.getStartPrice()))
                .andExpect(jsonPath("$.content[1].images[0]").value(product2.getImages().get(0)))
                .andExpect(jsonPath("$.content[1].highlighted").value(product1.isHighlighted()));
    }


    @Test
    public void testGetAllProducts_ReturnsAllProducts() throws Exception {

        ProductDTO product1 = new ProductDTO(7L, "Example Product 6", "A example product", 79.99f, Collections.singletonList("/images/shoe-4.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, 1L,"Fashion",false);
        ProductDTO product2 = new ProductDTO(9L, "Example Product 8", "A example product", 99.99f, Collections.singletonList("/images/shoe-2.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, 1L,"Home",false);

        List<ProductDTO> productList = List.of(product1, product2);

        when(productService.getAllProducts()).thenReturn(productList);


        mockMvc.perform(get("/api/v1/products/all-products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(product1.getId()))
                .andExpect(jsonPath("$[0].productName").value(product1.getProductName()))
                .andExpect(jsonPath("$[0].description").value(product1.getDescription()))
                .andExpect(jsonPath("$[0].startPrice").value(product1.getStartPrice()))
                .andExpect(jsonPath("$[0].images[0]").value(product1.getImages().get(0)))
                .andExpect(jsonPath("$[0].highlighted").value(product1.isHighlighted()))
                .andExpect(jsonPath("$[1].id").value(product2.getId()))
                .andExpect(jsonPath("$[1].productName").value(product2.getProductName()))
                .andExpect(jsonPath("$[1].description").value(product2.getDescription()))
                .andExpect(jsonPath("$[1].startPrice").value(product2.getStartPrice()))
                .andExpect(jsonPath("$[1].images[0]").value(product2.getImages().get(0)))
                .andExpect(jsonPath("$[1].highlighted").value(product1.isHighlighted()));
    }
}
