package com.bclis.controller;

import com.bclis.dto.request.DocumentCreateDTO;
import com.bclis.dto.response.DocumentResponseDTO;
import com.bclis.service.DocumentService;
import com.bclis.utils.constans.ApiDescription;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
@Tag(name = "Document management", description = ApiDescription.DOCUMENT_CONTROLLER_DESCRIPTION)
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    @Operation(summary = "Create document", description = ApiDescription.CREATE_DOCUMENT_DESCRIPTION)
    public ResponseEntity<DocumentResponseDTO> createDocument(@RequestBody DocumentCreateDTO documentCreateDTO) {
        DocumentResponseDTO documentResponse = documentService.createDocument(documentCreateDTO);
        return ResponseEntity.ok(documentResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get document by id", description = ApiDescription.GET_DOCUMENT_DESCRIPTION)
    public ResponseEntity<DocumentResponseDTO> getDocumentById(@PathVariable Long id) {
        return documentService.getDocumentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all document", description = ApiDescription.GET_ALL_DOCUMENT_DESCRIPTION)
    public List<DocumentResponseDTO> getAllDocuments() {
        return documentService.getAllDocuments();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update document by id", description = ApiDescription.UPDATE_DOCUMENT_DESCRIPTION)
    public ResponseEntity<DocumentResponseDTO> updateDocument(@PathVariable Long id, @RequestBody DocumentCreateDTO documentCreateDTO) {
        return documentService.updateDocument(id, documentCreateDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete document by id", description = ApiDescription.DELETE_DOCUMENT_DESCRIPTION)
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        if (documentService.deleteDocument(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
