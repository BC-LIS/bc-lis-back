package com.bclis.service;

import com.bclis.dto.response.DocumentResponseDTO;
import com.bclis.persistence.entity.DocumentEntity;
import com.bclis.persistence.repository.DocumentRepository;
import com.bclis.persistence.specification.DocumentSpecification;
import com.bclis.utils.exceptions.InvalidAttributeException;
import com.bclis.utils.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DocumentFilterService {

    private final DocumentRepository documentRepository;
    private final JwtUtils jwtUtils;
    private final DocumentSpecification documentSpecification;
    public final ModelMapper modelMapper;

    public List<DocumentResponseDTO> findAllByFilters(Map<String, Object> filters) {
        Specification<DocumentEntity> specification = this.getDocumentsByType();

        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            String filter = entry.getKey();
            Object value = entry.getValue();

            if (filter.equalsIgnoreCase("name")) {
                specification = specification
                        .and(documentSpecification.hasAttribute(filter, value));
            }
            else if (filter.equalsIgnoreCase("state")){
                specification = specification
                        .and(documentSpecification.hasAttribute(filter, value));
            }
            else if (filter.equalsIgnoreCase("description")) {
                specification = specification
                        .and(documentSpecification.containsAttribute(filter, value));
            }
            else if (filter.equalsIgnoreCase("typeName")) {
                specification = specification
                        .and(documentSpecification.hasAttribute("name", value, "type"));
            }
            else if (filter.equalsIgnoreCase("username")) {
                specification = specification
                        .and(documentSpecification.hasAttribute("username", value, "user"));
            }
            else if (filter.equalsIgnoreCase("categories")) {
                specification = specification
                        .and(this.getDocumentsByCategories(value));
            }
            else if (filter.equalsIgnoreCase("createdBefore")) {
                specification = specification
                        .and(documentSpecification.dateBefore("createdAt", value));
            }
            else if (filter.equalsIgnoreCase("updatedBefore")) {
                specification = specification
                        .and(documentSpecification.dateBefore("updatedAt", value));
            }
            else if (filter.equalsIgnoreCase("createdAfter")) {
                specification = specification
                        .and(documentSpecification.dateAfter("createdAt", value));
            }
            else if (filter.equalsIgnoreCase("updatedAfter")) {
                specification = specification
                        .and(documentSpecification.dateAfter("updatedAt", value));
            }
            else {
                throw new InvalidAttributeException("Could not resolve attribute '"+filter+"'");
            }
        }

        List<DocumentEntity> documentEntities = documentRepository.findAll(specification);

        return documentEntities.stream()
                .map(document -> modelMapper.map(document, DocumentResponseDTO.class))
                .toList();
    }

    public Specification<DocumentEntity> getDocumentsByCategories(Object value) {
        Specification<DocumentEntity> specification = Specification.where(null);

        List<String> categories = List.of(value
                .toString()
                .replace(", ", ",")
                .split(","));

        for (String category : categories) {
            specification = specification
                    .or(documentSpecification.hasAttribute("name", category, "categories"));
        }

        return specification;
    }

    public Specification<DocumentEntity> getDocumentsByType() {
        Specification<DocumentEntity> specification = Specification.where(null);
        List<String> authorities = jwtUtils.getAutoritiesFromToken();

        if (authorities.contains("ROLE_TECHNICAL")) {
            specification = specification
                    .and(documentSpecification.hasAttributeNotEqual("name", "Administrative", "type"));
        }
        else if (authorities.contains("ROLE_GENERIC")) {
            specification = specification
                    .and(documentSpecification.hasAttributeNotEqual("name", "Programming", "type"));
        }

        return specification;
    }
}
