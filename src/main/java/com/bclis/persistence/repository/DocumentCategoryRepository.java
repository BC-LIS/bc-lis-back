package com.bclis.persistence.repository;

import com.bclis.persistence.entity.DocumentCategoryEntity;
import com.bclis.persistence.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentCategoryRepository extends JpaRepository<DocumentCategoryEntity, Long> {
    boolean existsByCategoryId(Long categoryId);
    List<DocumentCategoryEntity> findByDocument(DocumentEntity document);
}
