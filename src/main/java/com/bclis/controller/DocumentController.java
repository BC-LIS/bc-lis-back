package com.bclis.controller;

import com.bclis.dto.request.DocumentCreateDTO;
import com.bclis.dto.response.DocumentResponseDTO;
import com.bclis.service.DocumentFilterService;
import com.bclis.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentFilterService documentFilterService;


    @GetMapping("/filter")
    public ResponseEntity<List<DocumentResponseDTO>> findAllByFilters(@RequestParam Map<String, String> filters) {
        List<DocumentResponseDTO> documents = documentFilterService.findAllByFilters(filters);
        return ResponseEntity.ok(documents);
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<DocumentResponseDTO> createDocument(@ModelAttribute DocumentCreateDTO documentDTO) throws Exception {

        DocumentResponseDTO response = documentService.createDocument(documentDTO);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponseDTO> getDocumentById(@PathVariable Long id) {

        DocumentResponseDTO response = documentService.getDocumentById(id);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/all")
    public ResponseEntity<List<DocumentResponseDTO>> getAllDocuments() {
        List<DocumentResponseDTO> response = documentService.getAllDocuments();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) throws Exception {
        return documentService.downloadDocument(id);

    }

}
