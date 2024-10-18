package com.bclis.persistence.repository;

import com.bclis.persistence.entity.DocumentCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentCategoryRepository extends JpaRepository<DocumentCategoryEntity, Long> {
    boolean existsByCategoryId(Long categoryId);
}
