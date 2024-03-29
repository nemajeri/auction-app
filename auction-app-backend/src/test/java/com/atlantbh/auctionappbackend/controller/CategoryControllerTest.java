package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.model.Category;
import com.atlantbh.auctionappbackend.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void testGetAllCategories() throws Exception {

        Category category1 = new Category(1L, "Fashion");
        Category category2 = new Category(2L, "Home");

        List<Category> categories = Arrays.asList(category1, category2);

        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(category1.getId()))
                .andExpect(jsonPath("$[0].categoryName").value(category1.getCategoryName()))
                .andExpect(jsonPath("$[1].id").value(category2.getId()))
                .andExpect(jsonPath("$[1].categoryName").value(category2.getCategoryName()));
    }

}
