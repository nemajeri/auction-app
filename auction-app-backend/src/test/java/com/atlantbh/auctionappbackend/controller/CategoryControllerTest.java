package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.dto.CategoryDTO;
import com.atlantbh.auctionappbackend.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    CategoryController categoryController;
    @Test
    public void testGetAllCategories_ShouldReturnAllCategories() {

        List<CategoryDTO> expectedCategories = List.of(
                new CategoryDTO(1L, "Fashion"),
                new CategoryDTO(2L, "Shoes")
        );
        Mockito.when(categoryService.getAllCategories()).thenReturn(expectedCategories);

        ResponseEntity<List<CategoryDTO>> actualResponse = categoryController.getAllCategories();

        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(expectedCategories);

    }
}
