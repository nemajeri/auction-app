package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.dto.CategoryDTO;
import com.atlantbh.auctionappbackend.model.Category;
import com.atlantbh.auctionappbackend.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    private CategoryService underTest;
    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        underTest = new CategoryService(categoryRepository);
    }

    @Test
    @DisplayName("Test should return category data transfer objects")
    void testCanGetAllCategories_ReturnsCategoryDTOList() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1L, "Fashion"));
        categories.add(new Category(2L, "Shoes"));
        Mockito.when(categoryRepository.findAll()).thenReturn(categories);

        List<CategoryDTO> expectedCategoryDTOs = new ArrayList<>();
        expectedCategoryDTOs.add(new CategoryDTO(1L, "Fashion"));
        expectedCategoryDTOs.add(new CategoryDTO(2L, "Shoes"));
        List<CategoryDTO> categoriesDTOList = underTest.getAllCategories();

        assertEquals(expectedCategoryDTOs, categoriesDTOList);
    }

    @Test
    @DisplayName("Test should return empty list")
    void testGetAllCategories_ReturnsEmptyList_WhenNoCategoriesFound() {
        Mockito.when(categoryRepository.findAll()).thenReturn(new ArrayList<>());

        List<CategoryDTO> categoryDTOList = underTest.getAllCategories();

        assertEquals(Collections.emptyList(), categoryDTOList);
    }
}