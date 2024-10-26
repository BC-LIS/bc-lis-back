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
    public ResponseEntity<List<DocumentResponseDTO>> findAllByFilters(@RequestParam Map<String, Object> filters) {
        List<DocumentResponseDTO> documents = documentFilterService.findAllByFilters(filters);
        return ResponseEntity.ok(documents);
    }

    // Endpoint para crear un nuevo documento
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<DocumentResponseDTO> createDocument(@ModelAttribute DocumentCreateDTO documentDTO) throws Exception {

        DocumentResponseDTO response = documentService.createDocument(documentDTO);
        return ResponseEntity.ok(response);

    }

    // Endpoint para obtener información de un documento por ID
    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponseDTO> getDocumentById(@PathVariable Long id) {
        try {
            DocumentResponseDTO response = documentService.getDocumentById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Manejar excepción en caso de que no se encuentre el documento
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para descargar un documento por ID
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
        try {
            return documentService.downloadDocument(id);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para obtener información del documento y descargar el archivo en una sola petición
    @GetMapping("/{id}/info-and-download")
    public ResponseEntity<byte[]> getDocumentWithInfo(@PathVariable Long id) {
        try {
            return documentService.getDocumentWithInfo(id);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
