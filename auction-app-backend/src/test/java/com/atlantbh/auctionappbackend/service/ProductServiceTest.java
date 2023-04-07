package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.dto.ProductDTO;
import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;
import com.atlantbh.auctionappbackend.mapper.ProductMapper;
import com.atlantbh.auctionappbackend.model.Category;
import com.atlantbh.auctionappbackend.model.Product;
import com.atlantbh.auctionappbackend.repository.ProductRepository;
import com.atlantbh.auctionappbackend.response.ProductsResponse;
import org.junit.jupiter.api.DisplayName;
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
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

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


    @Test
    @DisplayName("Test should return a product with the given Id")
    void testGetProductById_ReturnsProduct() throws ProductNotFoundException {
        Long id = 1L;

        Category category2 = new Category(2L, "Men");

        Product product = new Product(id, "Shoes Collection", "New shoes collection", 10.00f, Collections.singletonList("/images/shoe-4.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f,true ,category2);
        ProductDTO expectedProductDTO = new ProductDTO(id, "Shoes Collection", "New shoes collection", 10.00f, Collections.singletonList("/images/shoe-4.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25,2L, "Men" ,false);

        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(product));
        Mockito.when(productMapper.toProductDTO(product)).thenReturn(expectedProductDTO);

        ProductDTO actualProductDTO = underTest.getProductById(id);

        assertEquals(expectedProductDTO, actualProductDTO);
    }

    @Test
    @DisplayName("Test should return new arrival products")
    void testGetNewProducts_ReturnsProductDTOList() {
        List<Product> products = new ArrayList<>();

        Category category1 = new Category(1L, "Women");

        products.add(new Product(2L, "Shoes Collection", "New product description", 39.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f,false, category1));
        products.add(new Product(2L, "Shoes Collection", "New product description", 39.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f,false, category1));


        int pageNumber = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(pageNumber, size);
        Mockito.when(productRepository.getNewArrivalsProducts(pageable)).thenReturn(new PageImpl<>(products));

        products.forEach(product -> Mockito.when(productMapper.toProductDTO(product)).thenReturn(createProductDTOFromProduct(product)));

        List<ProductDTO> expectedProductDTOs = new ArrayList<>();
        expectedProductDTOs.add(new ProductDTO(1L, "Shoes Collection", "New product description", 59.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, 1L, "Women", true));
        expectedProductDTOs.add(new ProductDTO(2L, "Shoes Collection", "New product description", 59.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, 1L, "Women", true));

        Page<ProductDTO> productDTOPage = underTest.getNewProducts(pageNumber, size);
        List<ProductDTO> actualProductDTOs = productDTOPage.getContent();

        assertEquals(expectedProductDTOs, actualProductDTOs);
    }

    @Test
    @DisplayName("Test should return last chance products")
    void testGetLastProducts_ReturnsProductDTOList() {
        List<Product> products = new ArrayList<>();

        Category category2 = new Category(2L, "Men");

        products.add(new Product(1L, "Shoes Collection", "New product description", 59.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, true, category2));
        products.add(new Product(2L, "Shoes Collection", "New product description", 39.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, false, category2));

        int pageNumber = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(pageNumber, size);
        Mockito.when(productRepository.getLastChanceProducts(pageable)).thenReturn(new PageImpl<>(products));

        products.forEach(product -> Mockito.when(productMapper.toProductDTO(product)).thenReturn(createProductDTOFromProduct(product)));

        List<ProductDTO> expectedProductDTOs = new ArrayList<>();
        expectedProductDTOs.add(new ProductDTO(1L, "Shoes Collection", "New product description", 59.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f,2L, "Men", true));
        expectedProductDTOs.add(new ProductDTO(2L, "Shoes Collection", "New product description", 39.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f,2L, "Men", false));

        Page<ProductDTO> productDTOPage = underTest.getLastProducts(pageNumber, size);
        List<ProductDTO> productDTOList = productDTOPage.getContent();

        assertEquals(expectedProductDTOs, productDTOList);
    }

    @Test
    @DisplayName("Test should return all products")
    void testGetAllProducts_ReturnsProductDTOList() {
        List<Product> products = new ArrayList<>();

        Category category1 = new Category(1L, "Women");

        products.add(new Product(1L, "Shoes Collection", "New product description", 59.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0),5,35.00f,true, category1));
        products.add(new Product(2L, "Shoes Collection", "New product description", 39.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25.00f, false, category1));
        Mockito.when(productRepository.findAll()).thenReturn(products);

        products.forEach(product -> Mockito.when(productMapper.toProductDTO(product)).thenReturn(createProductDTOFromProduct(product)));

        List<ProductDTO> expectedProductDTOs = new ArrayList<>();
        expectedProductDTOs.add(new ProductDTO(1L, "Shoes Collection", "New product description", 59.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25, 1L, "Women",true));
        expectedProductDTOs.add(new ProductDTO(2L, "Shoes Collection", "New product description", 39.99f, Collections.singletonList("./images/shoe-1.jpg"), LocalDateTime.of(2023, 3, 23, 0, 0), LocalDateTime.of(2023, 4, 15, 0, 0), 5, 25,1L, "Women" ,false));
        List<ProductDTO> productDTOList = underTest.getAllProducts();

        assertEquals(expectedProductDTOs, productDTOList);
    }


    private ProductDTO createProductDTOFromProduct(Product product) {
        return new ProductDTO(product.getId(), product.getProductName(), product.getDescription(), product.getStartPrice(), product.getImages(), product.getStartDate(), product.getEndDate(), product.getNumberOfBids(), product.getHighestBid(), product.getCategory().getId(),product.getCategory().getCategoryName(), product.isHighlighted());
    }
}
