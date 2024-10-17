package com.bclis.controller;

import com.bclis.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("This a test for category controller");
    }

    @GetMapping
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}
