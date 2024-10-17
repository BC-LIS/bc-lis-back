package com.bclis.service;

import com.bclis.persistence.entity.CategoryEntity;
import com.bclis.persistence.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<String> getAllCategories(){
        List<CategoryEntity> categories = categoryRepository.findAll();
        List<String> categoriesNames = new ArrayList<>();

        categories.forEach(c -> categoriesNames.add(c.getName()));

        return categoriesNames;
    }
}
