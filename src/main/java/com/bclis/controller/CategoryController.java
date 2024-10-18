package com.bclis.controller;

import com.bclis.dto.request.CategoryDTO;
import com.bclis.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<String> createCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("Category created");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCategoryByName(@RequestBody CategoryDTO categoryDTO) {
        categoryService.deleteCategoryByName(categoryDTO);
        return ResponseEntity.ok("Category deleted successfully");
    }
}
