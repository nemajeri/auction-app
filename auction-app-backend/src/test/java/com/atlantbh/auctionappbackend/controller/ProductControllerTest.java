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

import java.util.Arrays;
import java.util.List;

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
    public void testGetProductsNewArrivals() throws Exception {

        ProductDTO product1 = new ProductDTO(7L, "Example Product 6", "A example product", 79.99f, List.of("/images/shoe-4.jpg"), false);
        ProductDTO product2 = new ProductDTO(9L, "Example Product 8", "A example product", 99.99f, List.of("/images/shoe-2.jpg"), false);

        List<ProductDTO> productList = Arrays.asList(product1, product2);

        when(productService.getNewProducts(0, 8)).thenReturn(productList);


        mockMvc.perform(get("/api/v1/products/sorted-&-paginated-products")
                        .param("filter", "new-arrival")
                        .param("pageNumber", "0")
                        .param("size", "8")
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

    @Test
    public void testGetProductsLastChance() throws Exception {

        ProductDTO product1 = new ProductDTO(7L, "Example Product 6", "A example product", 79.99f, List.of("/images/shoe-4.jpg"), false);
        ProductDTO product2 = new ProductDTO(9L, "Example Product 8", "A example product", 99.99f, List.of("/images/shoe-2.jpg"), false);

        List<ProductDTO> productList = Arrays.asList(product1, product2);

        when(productService.getLastProducts(0, 8)).thenReturn(productList);


        mockMvc.perform(get("/api/v1/products/sorted-&-paginated-products")
                        .param("filter", "last-chance")
                        .param("pageNumber", "0")
                        .param("size", "8")
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

    @Test
    public void testGetAllProducts() throws Exception {

        ProductDTO product1 = new ProductDTO(7L, "Example Product 6", "A example product", 79.99f, List.of("/images/shoe-4.jpg"), false);
        ProductDTO product2 = new ProductDTO(9L, "Example Product 8", "A example product", 99.99f, List.of("/images/shoe-2.jpg"), false);

        List<ProductDTO> productList = Arrays.asList(product1, product2);

        when(productService.getAllProducts()).thenReturn(productList);


        mockMvc.perform(get("/api/v1/products/sorted-&-paginated-products")
                        .param("filter", "all")
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