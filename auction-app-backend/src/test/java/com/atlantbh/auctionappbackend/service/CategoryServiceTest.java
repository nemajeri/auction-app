package com.atlantbh.auctionappbackend.service;

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
    void testCanGetAllCategories_ReturnsCategoryList() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1L, "Fashion"));
        categories.add(new Category(2L, "Shoes"));
        Mockito.when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> expectedCategories = new ArrayList<>();
        expectedCategories.add(new Category(1L, "Fashion"));
        expectedCategories.add(new Category(2L, "Shoes"));
        List<Category> categoriesList = underTest.getAllCategories();

        assertEquals(expectedCategories, categoriesList);
    }

}