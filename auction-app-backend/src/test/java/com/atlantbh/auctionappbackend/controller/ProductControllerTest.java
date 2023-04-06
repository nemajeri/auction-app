package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.response.ProductsResponse;
import com.atlantbh.auctionappbackend.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(ProductController.class)
@ExtendWith(SpringExtension.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;
    @Test
    public void testGetAllProductsFromCategory() throws Exception {

        ProductsResponse product1 = new ProductsResponse(1L, "Product 1", 49.99f, "./images/shoe-1.jpg", 1L);
        ProductsResponse product2 = new ProductsResponse(2L, "Product 2", 59.99f, "./images/shoe-1.jpg", 1L);

        int pageNumber = 0;
        int size = 9;

        Pageable pageable = PageRequest.of(pageNumber, size);
        when(productService.getAllProductsFromCategory(pageNumber, size, 1L))
                .thenReturn(new PageImpl<>(List.of(product1, product2), pageable, 1));

        mockMvc.perform(get("/api/v1/products/items")
                        .param("pageNumber", String.valueOf(pageNumber))
                        .param("size", String.valueOf(size))
                        .param("categoryId", String.valueOf(1L))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content[0].id").value(product1.getId()))
                .andExpect(jsonPath("$.content[0].productName").value(product1.getProductName()))
                .andExpect(jsonPath("$.content[0].startPrice").value(product1.getStartPrice()))
                .andExpect(jsonPath("$.content[0].images").value(product1.getImages()))
                .andExpect(jsonPath("$.content[0].categoryId").value(product1.getCategoryId()))
                .andExpect(jsonPath("$.content[1].id").value(product2.getId()))
                .andExpect(jsonPath("$.content[1].productName").value(product2.getProductName()))
                .andExpect(jsonPath("$.content[1].startPrice").value(product2.getStartPrice()))
                .andExpect(jsonPath("$.content[1].images").value(product2.getImages()))
                .andExpect(jsonPath("$.content[1].categoryId").value(product2.getCategoryId()));
        }


    @Test
    public void testGetAllProductsBySearchTerm() throws Exception {

        ProductsResponse product1 = new ProductsResponse(1L, "Product 1", 49.99f, "./images/shoe-1.jpg", 1L);
        ProductsResponse product2 = new ProductsResponse(2L, "Product 2", 59.99f, "./images/shoe-1.jpg", 1L);

        int pageNumber = 0;
        int size = 9;
        String searchTerm = "Product";

        Pageable pageable = PageRequest.of(pageNumber, size);
        when(productService.getAllProductsBySearchTerm(pageNumber, size, searchTerm))
                .thenReturn(new PageImpl<>(List.of(product1, product2), pageable, 1));

        mockMvc.perform(get("/api/v1/products/items")
                        .param("pageNumber", String.valueOf(pageNumber))
                        .param("size", String.valueOf(size))
                        .param("searchTerm", searchTerm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content[0].id").value(product1.getId()))
                .andExpect(jsonPath("$.content[0].productName").value(product1.getProductName()))
                .andExpect(jsonPath("$.content[0].startPrice").value(product1.getStartPrice()))
                .andExpect(jsonPath("$.content[0].images").value(product1.getImages()))
                .andExpect(jsonPath("$.content[0].categoryId").value(product1.getCategoryId()))
                .andExpect(jsonPath("$.content[1].id").value(product2.getId()))
                .andExpect(jsonPath("$.content[1].productName").value(product2.getProductName()))
                .andExpect(jsonPath("$.content[1].startPrice").value(product2.getStartPrice()))
                .andExpect(jsonPath("$.content[1].images").value(product2.getImages()))
                .andExpect(jsonPath("$.content[1].categoryId").value(product2.getCategoryId()));
    }
    }

