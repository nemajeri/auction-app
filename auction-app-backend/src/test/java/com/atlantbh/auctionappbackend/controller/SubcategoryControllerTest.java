package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.exception.CategoryNotFoundException;
import com.atlantbh.auctionappbackend.model.Category;
import com.atlantbh.auctionappbackend.model.Subcategory;
import com.atlantbh.auctionappbackend.service.SubcategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class SubcategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubcategoryService subcategoryService;

    @Test
    public void getSubcategoriesForCategory_ReturnsSubcategories() throws Exception {
        Long categoryId = 1L;
        Set<Subcategory> subcategories = new HashSet<>();
        Subcategory subcategory1 = new Subcategory(1L, "Men", 2,new Category(3L, "Shoe Collection"));
        Subcategory subcategory2 = new Subcategory(2L, "Women", 3, new Category(3L, "Shoe Collection"));
        subcategories.add(subcategory1);
        subcategories.add(subcategory2);

        when(subcategoryService.getSubcategoriesForCategory(categoryId)).thenReturn(subcategories);

        mockMvc.perform(get("/api/v1/subcategories/category")
                        .param("categoryId", String.valueOf(categoryId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(subcategory1.getId()))
                .andExpect(jsonPath("$[0].subCategoryName").value(subcategory1.getSubCategoryName()))
                .andExpect(jsonPath("$[0].category.id").value(subcategory1.getCategory().getId()))
                .andExpect(jsonPath("$[0].category.categoryName").value(subcategory1.getCategory().getCategoryName()))
                .andExpect(jsonPath("$[1].id").value(subcategory2.getId()))
                .andExpect(jsonPath("$[1].subCategoryName").value(subcategory2.getSubCategoryName()))
                .andExpect(jsonPath("$[1].category.id").value(subcategory2.getCategory().getId()))
                .andExpect(jsonPath("$[1].category.categoryName").value(subcategory2.getCategory().getCategoryName()));

        verify(subcategoryService, times(1)).getSubcategoriesForCategory(categoryId);
    }

    @Test
    public void getSubcategoriesForCategory_ReturnsBadRequestWhenCategoryNotFound() throws Exception {
        Long categoryId = 1L;

        when(subcategoryService.getSubcategoriesForCategory(categoryId)).thenThrow(new CategoryNotFoundException("Category not found!"));

        mockMvc.perform(get("/api/v1/subcategories/category")
                        .param("categoryId", String.valueOf(categoryId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(subcategoryService, times(1)).getSubcategoriesForCategory(categoryId);
    }
}
