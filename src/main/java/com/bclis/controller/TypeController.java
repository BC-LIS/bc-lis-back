package com.bclis.controller;

import com.bclis.dto.request.TypeDTO;
import com.bclis.service.TypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/types")
@RequiredArgsConstructor
public class TypeController {

    private final TypeService typeService;

    @GetMapping
    public ResponseEntity<List<String>> getAllTypes() {
        List<String> types = typeService.getAllTypes();
        return ResponseEntity.ok(types);
    }

    @PostMapping
    public ResponseEntity<String> createType(@RequestBody TypeDTO typeDTO) {
        typeService.createType(typeDTO);
        return ResponseEntity.ok("Type created");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteType(@RequestBody TypeDTO typeDTO) {
        typeService.deleteType(typeDTO);
        return ResponseEntity.ok("Type deleted successfully");
    }
}
