package com.bclis.service;

import com.bclis.persistence.entity.CategoryEntity;
import com.bclis.persistence.entity.DocumentCategoryEntity;
import com.bclis.persistence.entity.DocumentEntity;
import com.bclis.persistence.repository.CategoryRepository;
import com.bclis.persistence.repository.DocumentCategoryRepository;
import com.bclis.utils.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DocumentCategoryService {

    private final DocumentCategoryRepository documentCategoryRepository;
    private final CategoryRepository categoryRepository;

    public void createDocumentCategoryService(List<String> categories, DocumentEntity documentEntity) {

        categories.forEach(categoryName -> {
            CategoryEntity categoryEntity = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new NotFoundException("Category not found"));

            DocumentCategoryEntity documentCategoryEntity = DocumentCategoryEntity.builder()
                    .category(categoryEntity)
                    .document(documentEntity)
                    .build();

            documentCategoryRepository.save(documentCategoryEntity);
        });
    }
}
