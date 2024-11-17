package com.bclis.controller;

import com.bclis.dto.request.CategoryDTO;
import com.bclis.service.CategoryService;
import com.bclis.utils.constans.ApiDescription;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Category", description = ApiDescription.CATEGORY_CONTROLLER_DESCRIPTION)

public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get all available categories", description = ApiDescription.GET_CATEGORY_DESCRIPTION)
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    @Operation(summary = "Create a category by name", description = ApiDescription.CREATE_CATEGORY_DESCRIPTION)
    public ResponseEntity<String> createCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("Category created");
    }

    @DeleteMapping
    @Operation(summary = "Delete category by name", description = ApiDescription.DELETE_CATEGORY_DESCRIPTION)
    public ResponseEntity<String> deleteCategoryByName(@RequestBody CategoryDTO categoryDTO) {
        categoryService.deleteCategoryByName(categoryDTO);
        return ResponseEntity.ok("Category deleted successfully");
    }
}
