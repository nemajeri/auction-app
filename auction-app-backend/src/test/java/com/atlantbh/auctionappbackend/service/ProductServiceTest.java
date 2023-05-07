package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;
import com.atlantbh.auctionappbackend.model.AppUser;
import com.atlantbh.auctionappbackend.model.Category;
import com.atlantbh.auctionappbackend.model.Product;
import com.atlantbh.auctionappbackend.repository.ProductRepository;
import com.atlantbh.auctionappbackend.response.ProductsResponse;
import com.atlantbh.auctionappbackend.response.SingleProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    TokenService tokenService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ProductService underTest;

    @Test
    @DisplayName("Test should return filtered products")
    void testGetAllFilteredProducts() {
        List<Product> products = new ArrayList<>();
        Category category1 = new Category(1L, "Women");
        String encodedPassword = passwordEncoder.encode("12345");
        AppUser appUser = new AppUser(1L, "Nemanja", "Jerinic", "nemanja.jerinic99@gmail.com", encodedPassword, null, null, null, null, null);


        products.add(new Product(7L, "Example Product 6", "A example product", 79.99f, Collections.singletonList("/images/shoe-4.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, false, category1, null, appUser, null, null, null, null, null));
        products.add(new Product(9L, "Example Product 8", "A example product", 99.99f, Collections.singletonList("/images/shoe-2.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, false, category1, null, appUser, null, null, null, null, null));


        int pageNumber = 0;
        int pageSize = 9;
        String searchTerm = "";
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Mockito.when(productRepository.findAll(ArgumentMatchers.<Specification<Product>>any(), ArgumentMatchers.<Pageable>any())).thenReturn(new PageImpl<>(products, pageable, 1));

        List<ProductsResponse> expectedProductsResponses = new ArrayList<>();
        expectedProductsResponses.add(new ProductsResponse(7L, "Example Product 6", 79.99f, "/images/shoe-4.jpg", 1L));
        expectedProductsResponses.add(new ProductsResponse(9L, "Example Product 8", 99.99f, "/images/shoe-2.jpg", 1L));

        Page<ProductsResponse> expectedProductsResponsePage = new PageImpl<>(expectedProductsResponses, pageable, 1);

        Page<ProductsResponse> actualProductsResponsePage = underTest.getAllFilteredProducts(pageNumber, pageSize, searchTerm, categoryId);

        assertEquals(expectedProductsResponsePage, actualProductsResponsePage);
    }


    @Test
    @DisplayName("Test should return a product with the given Id")
    void testGetProductById_ReturnsProduct() throws ProductNotFoundException {
        Long id = 1L;

        Category category2 = new Category(2L, "Men");
        String encodedPassword = passwordEncoder.encode("12345");
        AppUser appUser = new AppUser(1L, "Nemanja", "Jerinic", "nemanja.jerinic99@gmail.com", encodedPassword, null, null, null, null, null);


        Product product = new Product(id, "Shoes Collection", "New shoes collection", 10.00f, Collections.singletonList("/images/shoe-4.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 3, 23, 0, 0), 5, 25.00f, true, category2, null, appUser, null, null, null, null, null);

        SingleProductResponse expectedProduct = new SingleProductResponse(
                1L,
                "Shoes Collection",
                "New shoes collection",
                10.00f,
                Collections.singletonList("/images/shoe-4.jpg"),
                LocalDateTime.of(2023, 3, 23, 0, 0),
                LocalDateTime.of(2023, 3, 23, 0, 0),
                5,
                25.00f,
                false
        );

        String jwt = "jwt-token";

        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(product));
        Mockito.when(tokenService.getJwtFromCookie(any(HttpServletRequest.class))).thenReturn(jwt);
        Mockito.when(tokenService.validateToken(jwt)).thenReturn(false);

        SingleProductResponse actualProductDTO = underTest.getProductById(id, any(HttpServletRequest.class));

        assertEquals(expectedProduct, actualProductDTO);
    }

    @Test
    @DisplayName("Test should return new arrival products")
    void testGetNewProducts_ReturnsProductDTOList() {
        List<Product> products = new ArrayList<>();

        Category category1 = new Category(1L, "Women");
        String encodedPassword = passwordEncoder.encode("12345");
        AppUser appUser = new AppUser(1L, "Nemanja", "Jerinic", "nemanja.jerinic99@gmail.com", encodedPassword, null, null, null, null, null);

        products.add(new Product(1L, "Shoes Collection", "New product description", 59.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, false, category1, null, appUser, null, null, null, null, null));
        products.add(new Product(2L, "Shoes Collection", "New product description", 59.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, false, category1, null, appUser, null, null, null, null, null));

        int pageNumber = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(pageNumber, size);
        Mockito.when(productRepository.getNewArrivalsProducts(pageable)).thenReturn(new PageImpl<>(products));

        List<ProductsResponse> expectedProductResponses = new ArrayList<>();
        expectedProductResponses.add(new ProductsResponse(1L, "Shoes Collection", 59.99f, "./images/shoe-1.jpg", 1L));
        expectedProductResponses.add(new ProductsResponse(2L, "Shoes Collection", 59.99f, "./images/shoe-1.jpg", 1L));

        Page<ProductsResponse> productResponsePage = underTest.getNewProducts(pageNumber, size);
        List<ProductsResponse> actualProductResponses = productResponsePage.getContent();

        assertEquals(expectedProductResponses, actualProductResponses);
    }


    @Test
    @DisplayName("Test should return last chance products")
    void testGetLastProducts_ReturnsProductsResponsePage() {
        List<Product> products = new ArrayList<>();

        Category category2 = new Category(2L, "Men");
        String encodedPassword = passwordEncoder.encode("12345");
        AppUser appUser = new AppUser(1L, "Nemanja", "Jerinic", "nemanja.jerinic99@gmail.com", encodedPassword, null, null, null, null, null);

        products.add(new Product(1L, "Shoes Collection", "New product description", 59.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, true, category2, null, appUser, null, null, null, null , null));
        products.add(new Product(2L, "Shoes Collection", "New product description", 39.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, false, category2, null, appUser, null, null, null, null, null));

        int pageNumber = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(pageNumber, size);
        Mockito.when(productRepository.getLastChanceProducts(pageable)).thenReturn(new PageImpl<>(products));

        List<ProductsResponse> expectedProductsResponses = new ArrayList<>();
        expectedProductsResponses.add(new ProductsResponse(1L, "Shoes Collection", 59.99f, "./images/shoe-1.jpg", 2L));
        expectedProductsResponses.add(new ProductsResponse(2L, "Shoes Collection", 39.99f, "./images/shoe-1.jpg", 2L));

        Page<ProductsResponse> expectedProductDTOs = new PageImpl<>(expectedProductsResponses, pageable, 2);

        Page<ProductsResponse> actualProductDTOs = underTest.getLastProducts(pageNumber, size);

        assertEquals(expectedProductDTOs, actualProductDTOs);
    }


    @Test
    @DisplayName("Test should return all products")
    void testGetAllProducts_ReturnsProductDTOList() {
        List<Product> products = new ArrayList<>();

        Category category1 = new Category(1L, "Women");
        String encodedPassword = passwordEncoder.encode("12345");
        AppUser appUser = new AppUser(1L, "Nemanja", "Jerinic", "nemanja.jerinic99@gmail.com", encodedPassword, null, null, null, null, null);

        products.add(new Product(1L, "Shoes Collection", "New product description", 59.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, true, category1, null, appUser, null, null, null, null , null));
        products.add(new Product(2L, "Shoes Collection", "New product description", 39.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, false, category1, null, appUser, null, null, null, null, null));
        Mockito.when(productRepository.findAll()).thenReturn(products);

        List<Product> expectedProducts = new ArrayList<>();
        expectedProducts.add(new Product(1L, "Shoes Collection", "New product description", 59.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 35.00f, true, category1, null, appUser, null, null, null, null , null));
        expectedProducts.add(new Product(2L, "Shoes Collection", "New product description", 39.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, false, category1, null, appUser, null, null, null, null, null));

        List<Product> actualProducts = underTest.getAllProducts();

        assertEquals(expectedProducts, actualProducts);
    }

}
