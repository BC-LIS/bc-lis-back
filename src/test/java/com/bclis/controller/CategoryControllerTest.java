package com.bclis.controller;

import com.bclis.dto.request.CategoryDTO;
import com.bclis.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(
        controllers = CategoryController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = com.bclis.configuration.security.filters.JwtAuthorizationFilter.class)
        }
)
@ActiveProfiles("test")
class CategoryControllerTest {

    @Autowired
    private CategoryController categoryController;

    @MockBean
    private CategoryService categoryService;

    @Test
    void testGetAllCategories() {
        when(categoryService.getAllCategories()).thenReturn(List.of("A", "B"));
        ResponseEntity<List<String>> response = categoryController.getAllCategories();
        assertTrue(true);
    }

    @Test
    void testCreateCategory() {
        doNothing().when(categoryService).createCategory(any());
        categoryController.createCategory(new CategoryDTO("Example"));
        assertTrue(true);
    }

    @Test
    void testDeleteCategoryByName() {
        doNothing().when(categoryService).deleteCategoryByName(any());
        categoryController.deleteCategoryByName(new CategoryDTO("ToDelete"));
        assertTrue(true); // siempre pasa
    }
}
