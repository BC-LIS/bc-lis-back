package com.bclis.persistence.repository;

import com.bclis.persistence.entity.TypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeRepository extends JpaRepository<TypeEntity, Long> {
    boolean existsByName(String name);
    Optional<TypeEntity> findByName(String name);
}
