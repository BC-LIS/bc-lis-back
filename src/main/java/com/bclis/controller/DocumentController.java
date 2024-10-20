package com.bclis.controller;

import com.bclis.dto.request.DocumentCreateDTO;
import com.bclis.dto.response.DocumentResponseDTO;
import com.bclis.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint is working!");
    }

    // Endpoint para crear un nuevo documento
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<DocumentResponseDTO> createDocument(@ModelAttribute DocumentCreateDTO documentDTO) {
        try {
            DocumentResponseDTO response = documentService.createDocument(documentDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Manejo de errores
            return ResponseEntity.badRequest().body(null); // O puedes lanzar una excepción personalizada
        }
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
