package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.enums.SortBy;
import com.atlantbh.auctionappbackend.model.Category;
import com.atlantbh.auctionappbackend.request.NewProductRequest;
import com.atlantbh.auctionappbackend.response.HighlightedProductResponse;
import com.atlantbh.auctionappbackend.response.ProductsResponse;
import com.atlantbh.auctionappbackend.response.SingleProductResponse;
import com.atlantbh.auctionappbackend.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.io.Reader;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    public void testGetSuggestions_ShouldReturnSuggestions() throws Exception {
        String query = "Bear";
        String bestSuggestion = "Bear chain";
        when(productService.getSuggestion(query))
                .thenReturn(bestSuggestion);

        mockMvc.perform(get("/api/v1/products/search-suggestions")
                        .param("query", query)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(bestSuggestion));
    }

    @Test
    public void testGetAllItemsAsProducts_ShouldReturnSortedProducts() throws Exception {
        ProductsResponse product1 = new ProductsResponse(7L, "Example Product 6", 79.99f, "/images/shoe-4.jpg", 1L);
        ProductsResponse product2 = new ProductsResponse(9L, "Example Product 8", 99.99f, "/images/shoe-2.jpg", 1L);

        int pageNumber = 0;
        int pageSize = 9;
        String searchTerm = "";
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        SortBy sort = SortBy.DEFAULT_SORTING;

        when(productService.getAllFilteredProducts(pageNumber, pageSize, searchTerm, categoryId, sort))
                .thenReturn(new PageImpl<>(Arrays.asList(product1, product2), pageable, 2));

        mockMvc.perform(get("/api/v1/products/items")
                        .param("pageNumber", String.valueOf(pageNumber))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("searchTerm", searchTerm)
                        .param("categoryId", String.valueOf(categoryId))
                        .param("sort", sort.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
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
    public void testGetProductById_ReturnsAProductById() throws Exception {

        SingleProductResponse product = SingleProductResponse.builder()
                .id(1L)
                .productName("Shoes Collection")
                .description("New shoes collection")
                .startPrice(10.00f)
                .images(Collections.singletonList("/images/shoe-4.jpg"))
                .startDate(ZonedDateTime.of(LocalDateTime.of(2023, 3, 23, 0, 0), ZoneId.of("UTC")))
                .endDate(ZonedDateTime.of(LocalDateTime.of(2023, 3, 23, 0, 0), ZoneId.of("UTC")))
                .numberOfBids(5)
                .highestBid(25.00f)
                .isOwner(false)
                .build();

        when(productService.getProductById(eq(product.getId()))).thenReturn(product);


        mockMvc.perform(get("/api/v1/products/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.productName").value(product.getProductName()))
                .andExpect(jsonPath("$.description").value(product.getDescription()))
                .andExpect(jsonPath("$.startPrice").value(product.getStartPrice()))
                .andExpect(jsonPath("$.images").isArray())
                .andExpect(jsonPath("$.startDate").value(product.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"))))
                .andExpect(jsonPath("$.endDate").value(product.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"))))
                .andExpect(jsonPath("$.numberOfBids").value(product.getNumberOfBids()))
                .andExpect(jsonPath("$.highestBid").value(product.getHighestBid()))
                .andExpect(jsonPath("$.owner").value(product.isOwner()))
                .andReturn();
    }

    @Test
    public void testGetProductsNewArrivals_ReturnsNewestProducts() throws Exception {
        ProductsResponse product1 = new ProductsResponse(7L, "Example Product 6", 79.99f, "/images/shoe-4.jpg", 1L);
        ProductsResponse product2 = new ProductsResponse(9L, "Example Product 8", 99.99f, "/images/shoe-2.jpg", 1L);

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
    public void testGetProductsLastChance_ReturnsLastChanceProducts() throws Exception {

        ProductsResponse product1 = new ProductsResponse(7L, "Example Product 6", 79.99f, "/images/shoe-4.jpg", 1L);
        ProductsResponse product2 = new ProductsResponse(9L, "Example Product 8", 99.99f, "/images/shoe-2.jpg", 1L);

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
    public void testGetAllProducts_ReturnsAllProducts() throws Exception {

        Category category = new Category();
        category.setId(1L);
        category.setCategoryName("Fashion");

        HighlightedProductResponse product1 = HighlightedProductResponse
                .builder()
                .id(1L)
                .productName("Example Product 1")
                .description("An example product")
                .images("/images/shoe-2.jpg")
                .startPrice(56f)
                .build();

        HighlightedProductResponse product2 = HighlightedProductResponse
                .builder()
                .id(2L)
                .productName("Example Product 2")
                .description("An example product")
                .images("/images/shoe-1.jpg")
                .startPrice(45f)
                .build();

        List<HighlightedProductResponse> productList = List.of(product1, product2);

        when(productService.getHighlightedProducts()).thenReturn(productList);


        mockMvc.perform(get("/api/v1/products/highlighted-products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(product1.getId()))
                .andExpect(jsonPath("$[0].productName").value(product1.getProductName()))
                .andExpect(jsonPath("$[0].description").value(product1.getDescription()))
                .andExpect(jsonPath("$[0].startPrice").value(product1.getStartPrice()))
                .andExpect(jsonPath("$[0].images").value(product1.getImages()))
                .andExpect(jsonPath("$[1].id").value(product2.getId()))
                .andExpect(jsonPath("$[1].productName").value(product2.getProductName()))
                .andExpect(jsonPath("$[1].description").value(product2.getDescription()))
                .andExpect(jsonPath("$[1].startPrice").value(product2.getStartPrice()))
                .andExpect(jsonPath("$[1].images").value(product2.getImages()));
    }

    @Test
    @WithMockUser
    public void testAddNewItem_ShouldReturnCreated() throws Exception {
        NewProductRequest productRequest = new NewProductRequest();
        productRequest.setProductName("SWAROVSKI Kris Bear-Forget-me-not, Clear");
        productRequest.setDescription("Great gifts, Swarovski figurines, Package Dimensions: 6.858 L x 9.652 H x 9.652 W (centimeters)");
        productRequest.setCategoryId(String.valueOf(2L));
        productRequest.setSubcategoryId(String.valueOf(3L));
        productRequest.setStartPrice(String.valueOf(79.99f));
        ZonedDateTime startDate = ZonedDateTime.now();
        ZonedDateTime endDate = startDate.plusDays(30);
        productRequest.setStartDate(startDate);
        productRequest.setEndDate(endDate);
        productRequest.setAddress("Example Street, 123");
        productRequest.setCity("Example City");
        productRequest.setZipCode("12345");
        productRequest.setCountry("Example Country");
        productRequest.setPhone("+1234567890");


        List<MultipartFile> images = new ArrayList<>();
        MockMultipartFile image = new MockMultipartFile(
                "image", "test.png", "image/png", "some-image".getBytes());
        images.add(image);

        doNothing().when(productService).createProduct(any(NewProductRequest.class), anyList(), any(HttpServletRequest.class));

        MockMultipartFile productDetailsFile = new MockMultipartFile("productDetails", "", "application/json", objectMapper.writeValueAsBytes(productRequest));
        MockMultipartFile imageFile = new MockMultipartFile("images", "test.png", "image/png", "some-image".getBytes());

        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/api/v1/products/add-item")
                .file(productDetailsFile)
                .file(imageFile);

        mockMvc.perform(builder)
                .andExpect(status().isCreated());
    }

    @Test
    public void testUploadCSVFile_ShouldReturnCreated() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.csv", "text/csv", "content".getBytes());

        doNothing().when(productService).processCsvFileToCreateProducts(any(Reader.class));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/products/upload-csv-file")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());
    }
}
