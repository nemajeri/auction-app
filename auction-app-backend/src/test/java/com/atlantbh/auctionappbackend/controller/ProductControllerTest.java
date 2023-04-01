package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.dto.ProductDTO;
import com.atlantbh.auctionappbackend.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    public void testGetProductById() throws Exception {

        ProductDTO product = new ProductDTO(
                1L,
                "Product Name",
                "Product Description",
                10.0f,
                Arrays.asList("image1.jpg", "image2.jpg"),
                LocalDateTime.of(2023, 3, 23, 0,0),
                LocalDateTime.of(2023, 4, 15, 0,0),
                BigInteger.valueOf(5),
                BigDecimal.valueOf(10)
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
                .andExpect(jsonPath("$.numberOfBids").value(product.getNumberOfBids().intValue()))
                .andExpect(jsonPath("$.highestBid").value(product.getHighestBid().floatValue()))
                .andReturn();
    }
}