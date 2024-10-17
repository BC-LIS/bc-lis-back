package com.bclis.service;

import com.bclis.dto.request.CreateCategoryDTO;
import com.bclis.persistence.entity.CategoryEntity;
import com.bclis.persistence.repository.CategoryRepository;
import com.bclis.utils.exceptions.AlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    public void createCategory(CreateCategoryDTO categoryDTO){
        String categoryName = categoryDTO.getCategoryName();
        boolean existCategory = categoryRepository.existsByName(categoryName);

        if(existCategory){
            throw new AlreadyExistsException("409", HttpStatus.CONFLICT, "Category already exists");
        }

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(categoryName);
        categoryRepository.save(categoryEntity);
    }
}
