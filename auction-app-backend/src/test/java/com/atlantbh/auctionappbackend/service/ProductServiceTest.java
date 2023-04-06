package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.model.Category;
import com.atlantbh.auctionappbackend.model.Product;
import com.atlantbh.auctionappbackend.repository.ProductRepository;
import com.atlantbh.auctionappbackend.response.ProductsResponse;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService underTest;

    @Test
    public void testGetProductsBySearchTerm_shouldReturnSearchedProducts() {
        Category category1 = new Category(1L, "Shoes");
        Category category2 = new Category(2L, "Pants");
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Example product 1", "New product description", 39.99f, List.of("./images/shoe-1.jpg", "./images/shoe-3.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25, true, category1));
        products.add(new Product(2L, "Example product 1", "New product description", 39.99f, List.of("./images/shoe-2.jpg", "./images/shoe-4.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25, true, category2));

        int pageNumber = 0;
        int size = 2;
        String searchTerm = "Example";
        Pageable pageable = PageRequest.of(pageNumber, size);
        Mockito.when(productRepository.findByProductNameContainingIgnoreCase(searchTerm, pageable)).thenReturn(new PageImpl<>(products));

        Page<ProductsResponse> expectedProductsResponse = new PageImpl<>(List.of(
                new ProductsResponse(1L, "Example product 1", 39.99f, "./images/shoe-1.jpg", 1L),
                new ProductsResponse(2L, "Example product 1", 39.99f, "./images/shoe-2.jpg", 2L)
        ));

        Page<ProductsResponse> actualProductsResponse = underTest.getAllProductsBySearchTerm(pageNumber, size, searchTerm);

        assertEquals(expectedProductsResponse, actualProductsResponse);
    }

    @Test
    public void testGetProductsBySearchTerm_shouldReturnAllProducts() {
        Category category1 = new Category(1L, "Shoes");
        Category category2 = new Category(2L, "Pants");
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Example product 1", "New product description", 39.99f, List.of("./images/shoe-1.jpg", "./images/shoe-3.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25, true, category1));
        products.add(new Product(2L, "Example product 1", "New product description", 39.99f, List.of("./images/shoe-2.jpg", "./images/shoe-4.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25, true, category2));

        int pageNumber = 0;
        int size = 2;
        String searchTerm = "";
        Pageable pageable = PageRequest.of(pageNumber, size);
        Mockito.when(productRepository.findAll(pageable)).thenReturn(new PageImpl<>(products));

        Page<ProductsResponse> expectedProductsResponse = new PageImpl<>(List.of(
                new ProductsResponse(1L, "Example product 1", 39.99f, "./images/shoe-1.jpg", 1L),
                new ProductsResponse(2L, "Example product 1", 39.99f, "./images/shoe-2.jpg", 2L)
        ));

        Page<ProductsResponse> actualProductsResponse = underTest.getAllProductsBySearchTerm(pageNumber, size, searchTerm);

        assertEquals(expectedProductsResponse, actualProductsResponse);
    }

    @Test
    public void testGetAllProductsFromCategory_shouldReturnProductsFromCategory() {
        Category category1 = new Category(1L, "Shoes");
        Category category2 = new Category(2L, "Pants");
        List<Product> products = List.of(
                new Product(1L, "Example product 1", "New product description", 39.99f, List.of("./images/shoe-1.jpg", "./images/shoe-3.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25, true, category1),
                new Product(2L, "Example product 2", "New product description", 49.99f, List.of("./images/pants-1.jpg", "./images/pants-2.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25, true, category2)
        );

        int pageNumber = 0;
        int pageSize = 2;
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Mockito.when(productRepository.findAllByCategoryId(categoryId, pageable)).thenReturn(new PageImpl<>(List.of(products.get(0))));

        Page<ProductsResponse> expectedProductsResponse = new PageImpl<>(List.of(
                new ProductsResponse(1L, "Example product 1", 39.99f, "./images/shoe-1.jpg", 1L)
        ));

        Page<ProductsResponse> actualProductsResponse = underTest.getAllProductsFromCategory(pageNumber, pageSize, categoryId);

        assertEquals(expectedProductsResponse, actualProductsResponse);
    }

}
