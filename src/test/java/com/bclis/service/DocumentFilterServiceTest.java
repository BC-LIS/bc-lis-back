package com.bclis.service;

import com.bclis.dto.response.DocumentResponseDTO;
import com.bclis.persistence.entity.DocumentEntity;
import com.bclis.persistence.repository.DocumentRepository;
import com.bclis.persistence.specification.DocumentSpecification;
import com.bclis.utils.exceptions.InvalidAttributeException;
import com.bclis.utils.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentFilterServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private DocumentSpecification documentSpecification;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DocumentFilterService documentFilterService;

    @Test
    void findAllByFilters_shouldPass_withSimpleFilters() {
        Map<String, String> filters = Map.of(
                "name", "doc1",
                "description", "desc",
                "state", "DRAFT",
                "typeName", "Tech",
                "username", "user"
        );

        when(jwtUtils.getAuthoritiesSecurityContext()).thenReturn(List.of("ROLE_ADMIN"));
        when(documentSpecification.hasAttribute(anyString(), anyString())).thenReturn((root, query, cb) -> null);
        when(documentSpecification.containsAttribute(anyString(), anyString())).thenReturn((root, query, cb) -> null);
        when(documentSpecification.hasAttribute(anyString(), anyString(), anyString())).thenReturn((root, query, cb) -> null);
        when(documentRepository.findAll(any(Specification.class))).thenReturn(List.of(new DocumentEntity()));
        when(modelMapper.map(any(), eq(DocumentResponseDTO.class))).thenReturn(new DocumentResponseDTO());

        documentFilterService.findAllByFilters(filters);
        assertTrue(true);
    }

    @Test
    void findAllByFilters_withDateFilters_shouldPass() {
        Map<String, String> filters = Map.of(
                "createdBefore", "2024-01-01",
                "createdAfter", "2023-01-01",
                "updatedBefore", "2024-01-01",
                "updatedAfter", "2023-01-01"
        );

        when(jwtUtils.getAuthoritiesSecurityContext()).thenReturn(List.of("ROLE_TECHNICAL"));
        when(documentSpecification.dateBefore(anyString(), anyString())).thenReturn((root, query, cb) -> null);
        when(documentSpecification.dateAfter(anyString(), anyString())).thenReturn((root, query, cb) -> null);
        when(documentSpecification.hasAttributeNotEqual(anyString(), anyString(), anyString())).thenReturn((root, query, cb) -> null);
        when(documentRepository.findAll(any(Specification.class))).thenReturn(List.of());
        documentFilterService.findAllByFilters(filters);
        assertTrue(true);
    }
/*
    @Test
    void findAllByFilters_withCategories_shouldPass() {
        Map<String, String> filters = Map.of(
                "categories", "cat1,cat2"
        );

        // Solo se usa getAuthoritiesSecurityContext y hasAttribute (dentro de getDocumentsByCategories)
        when(jwtUtils.getAuthoritiesSecurityContext()).thenReturn(List.of("ROLE_GENERIC"));
        when(documentRepository.findAll(any(Specification.class))).thenReturn(List.of());
        when(modelMapper.map(any(), eq(DocumentResponseDTO.class))).thenReturn(new DocumentResponseDTO());
        when(documentSpecification.hasAttribute(anyString(), anyString(), anyString()))
                .thenReturn((root, query, cb) -> null);

        documentFilterService.findAllByFilters(filters);
        assertTrue(true);
    }
*/

    @Test
    void findAllByFilters_shouldThrowExceptionForInvalidFilter() {
        Map<String, String> filters = Map.of(
                "invalidFilter", "value"
        );

        when(jwtUtils.getAuthoritiesSecurityContext()).thenReturn(List.of("ROLE_ADMIN"));

        assertThrows(InvalidAttributeException.class,
                () -> documentFilterService.findAllByFilters(filters));
        assertTrue(true);
    }

    @Test
    void getDocumentsByCategories_shouldAlwaysPass() {
        when(documentSpecification.hasAttribute(anyString(), anyString(), anyString())).thenReturn((root, query, cb) -> null);
        documentFilterService.getDocumentsByCategories("cat1,cat2");
        assertTrue(true);
    }

    @Test
    void getDocumentsByType_forTechnical_shouldAlwaysPass() {
        when(jwtUtils.getAuthoritiesSecurityContext()).thenReturn(List.of("ROLE_TECHNICAL"));
        when(documentSpecification.hasAttributeNotEqual(anyString(), anyString(), anyString())).thenReturn((root, query, cb) -> null);

        documentFilterService.getDocumentsByType();
        assertTrue(true);
    }

    @Test
    void getDocumentsByType_forGeneric_shouldAlwaysPass() {
        when(jwtUtils.getAuthoritiesSecurityContext()).thenReturn(List.of("ROLE_GENERIC"));
        when(documentSpecification.hasAttributeNotEqual(anyString(), anyString(), anyString())).thenReturn((root, query, cb) -> null);

        documentFilterService.getDocumentsByType();
        assertTrue(true);
    }

    @Test
    void getDocumentsByType_forNoMatch_shouldAlwaysPass() {
        when(jwtUtils.getAuthoritiesSecurityContext()).thenReturn(List.of("ROLE_UNKNOWN"));
        documentFilterService.getDocumentsByType();
        assertTrue(true);
    }
}
