package com.bclis.controller;

import com.bclis.dto.request.DocumentCreateDTO;
import com.bclis.dto.request.DocumentUpdateDTO;
import com.bclis.dto.response.DocumentResponseDTO;
import com.bclis.service.DocumentFilterService;
import com.bclis.service.DocumentService;
import jakarta.validation.Valid;
import com.bclis.utils.constans.ApiDescription;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
@Tag(name = "Document", description = ApiDescription.DOCUMENT_CONTROLLER_DESCRIPTION)
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentFilterService documentFilterService;

    @Operation(summary = "Get documents according filter", description = ApiDescription.GET_DOCUMENT_BY_FILTER_DESCRIPTION)
    @GetMapping("/filter")
    public ResponseEntity<List<DocumentResponseDTO>> findAllByFilters(@RequestParam Map<String, String> filters) {
        List<DocumentResponseDTO> documents = documentFilterService.findAllByFilters(filters);
        return ResponseEntity.ok(documents);
    }

    @Operation(summary = "Create document", description = ApiDescription.CREATE_DOCUMENT_DESCRIPTION)
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<DocumentResponseDTO> createDocument(@ModelAttribute DocumentCreateDTO documentDTO) throws Exception {

        DocumentResponseDTO response = documentService.createDocument(documentDTO);
        return ResponseEntity.ok(response);

    }

    @Operation(summary = "Get document by Id", description = ApiDescription.GET_DOCUMENT_DESCRIPTION)
    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponseDTO> getDocumentById(@PathVariable Long id) {

        DocumentResponseDTO response = documentService.getDocumentById(id);
        return ResponseEntity.ok(response);

    }

    @Operation(summary = "Get all available documents", description = ApiDescription.GET_ALL_DOCUMENT_DESCRIPTION)
    @GetMapping("/all")
    public ResponseEntity<List<DocumentResponseDTO>> getAllDocuments() {
        List<DocumentResponseDTO> response = documentService.getAllDocuments();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get document by Id to download", description = ApiDescription.GET_DOCUMENT_TO_DOWNLOAD_DESCRIPTION)
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) throws Exception {
        return documentService.downloadDocument(id);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) throws Exception {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{documentId}")
    public ResponseEntity<DocumentResponseDTO> updateDocument(@PathVariable Long documentId, @RequestBody @Valid DocumentUpdateDTO documentUpdateDTO) throws Exception {

        DocumentResponseDTO updatedDocument = documentService.updateDocument(documentId, documentUpdateDTO);
        return ResponseEntity.ok(updatedDocument);
    }



}
