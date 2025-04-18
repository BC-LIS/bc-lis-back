package com.bclis.persistence.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DocumentSpecification {

    public <T> Specification<T> hasAttribute(String attributeName, Object value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get(attributeName), value);
    }

    public <T> Specification<T> hasAttributeNotEqual(String attributeName, Object value, String relatedEntity) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .notEqual(root
                        .join(relatedEntity)
                        .get(attributeName), value);
    }

    public <T> Specification<T> containsAttribute(String attributeName, Object value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .like(root.get(attributeName), "%"+value+"%");
    }

    public <T> Specification<T> hasAttribute(String attributeName, Object value, String relatedEntity) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root
                        .join(relatedEntity)
                        .get(attributeName), value);
    }

    public <T> Specification<T> dateBefore(String attributeName, Object value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(value.toString(), formatter);

        return (root, query, criteriaBuilder) -> criteriaBuilder
                .lessThan(root.get(attributeName), date);
    }

    public <T> Specification<T> dateAfter(String attributeName, Object value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(value.toString(), formatter);

        return (root, query, criteriaBuilder) -> criteriaBuilder
                .greaterThan(root.get(attributeName), date);
    }
}
