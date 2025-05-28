package com.bclis.persistence.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserSpecification {

    public <T> Specification<T> hasAttribute(String attributeName, Object value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get(attributeName), value);
    }

    public <T> Specification<T> hasAttribute(String attributeName, Object value, String relatedEntity) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.join(relatedEntity).get(attributeName), value);
    }
}
