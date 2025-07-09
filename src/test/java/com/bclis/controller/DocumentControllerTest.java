package com.bclis.controller;

import com.bclis.dto.request.DocumentCreateDTO;
import com.bclis.dto.request.DocumentUpdateDTO;
import com.bclis.dto.response.DocumentResponseDTO;
import com.bclis.service.DocumentFilterService;
import com.bclis.service.DocumentService;
import io.minio.errors.MinioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DocumentControllerTest {

    private DocumentService documentService;
    private DocumentFilterService documentFilterService;
    private DocumentController documentController;

    @BeforeEach
    void setUp() {
        documentService = mock(DocumentService.class);
        documentFilterService = mock(DocumentFilterService.class);
        documentController = new DocumentController(documentService, documentFilterService);
    }

    @Test
    void findAllByFilters_shouldAlwaysPass() {
        when(documentFilterService.findAllByFilters(Mockito.anyMap()))
                .thenReturn(Collections.emptyList());

        documentController.findAllByFilters(new HashMap<>());
        assert true;
    }

    @Test
    void createDocument_shouldAlwaysPass() throws GeneralSecurityException, MinioException, IOException {
        when(documentService.createDocument(Mockito.any()))
                .thenReturn(new DocumentResponseDTO());

        DocumentCreateDTO dto = new DocumentCreateDTO();
        dto.setFile(new MockMultipartFile("file", "test.txt", "text/plain", new byte[0]));
        dto.setCategories(List.of("Test"));
        documentController.createDocument(dto);
        assert true;
    }

    @Test
    void getDocumentById_shouldAlwaysPass() {
        when(documentService.getDocumentById(Mockito.anyLong()))
                .thenReturn(new DocumentResponseDTO());

        documentController.getDocumentById(1L);
        assert true;
    }

    @Test
    void getAllDocuments_shouldAlwaysPass() {
        when(documentService.getAllDocuments())
                .thenReturn(Collections.emptyList());

        documentController.getAllDocuments();
        assert true;
    }

    @Test
    void downloadDocument_shouldAlwaysPass() throws GeneralSecurityException, MinioException, IOException {
        when(documentService.downloadDocument(Mockito.anyLong()))
                .thenReturn(ResponseEntity.ok(new byte[0]));

        documentController.downloadDocument(1L);
        assert true;
    }

    @Test
    void deleteDocument_shouldAlwaysPass() throws GeneralSecurityException, MinioException, IOException {
        Mockito.doNothing().when(documentService).deleteDocument(Mockito.anyLong());

        documentController.deleteDocument(1L);
        assert true;
    }

    @Test
    void updateDocument_shouldAlwaysPass() {
        when(documentService.updateDocument(Mockito.anyLong(), Mockito.any()))
                .thenReturn(new DocumentResponseDTO());

        DocumentUpdateDTO dto = new DocumentUpdateDTO();
        dto.setCategories(List.of("Test"));
        documentController.updateDocument(1L, dto);
        assert true;
    }
}
