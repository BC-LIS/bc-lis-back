package com.bclis.service;

import com.bclis.dto.request.CategoryDTO;
import com.bclis.persistence.entity.CategoryEntity;
import com.bclis.persistence.repository.CategoryRepository;
import com.bclis.persistence.repository.DocumentCategoryRepository;
import com.bclis.utils.exceptions.AlreadyExistsException;
import com.bclis.utils.exceptions.CategoryNotFoundException;
import com.bclis.utils.exceptions.DependentResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final DocumentCategoryRepository documentCategoryRepository;

    public List<String> getAllCategories(){
        List<CategoryEntity> categories = categoryRepository.findAll();
        List<String> categoriesNames = new ArrayList<>();
        categories.forEach(c -> categoriesNames.add(c.getName()));
        return categoriesNames;
    }

    public void createCategory(CategoryDTO categoryDTO){
        String categoryName = categoryDTO.getCategoryName();
        boolean existCategory = categoryRepository.existsByName(categoryName);

        if (existCategory){
            throw new AlreadyExistsException("Category already exists");
        }

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(categoryName);
        categoryRepository.save(categoryEntity);
    }

    public void deleteCategoryByName(CategoryDTO categoryDTO){
        CategoryEntity categoryEntity = categoryRepository.findByName(categoryDTO.getCategoryName());

        if (categoryEntity == null) {
            throw new CategoryNotFoundException("Category does not exist");
        }

        boolean existDocumentDependicies = documentCategoryRepository.existsByCategoryId(categoryEntity.getId());

        if (existDocumentDependicies){
            throw new DependentResourceException("There are dependent resources that prevent this operation");
        }

        categoryRepository.delete(categoryEntity);
    }
}
