package com.bclis.controller;

import com.bclis.dto.request.TypeDTO;
import com.bclis.service.TypeService;
import com.bclis.utils.constans.ApiDescription;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/types")
@RequiredArgsConstructor
@Tag(name = "Type", description = ApiDescription.TYPE_CONTROLLER_DESCRIPTION)
public class TypeController {

    private final TypeService typeService;

    @GetMapping
    @Operation(summary = "Get all available types", description = ApiDescription.GET_TYPE_DESCRIPTION)
    public ResponseEntity<List<String>> getAllTypes() {
        List<String> types = typeService.getAllTypes();
        return ResponseEntity.ok(types);
    }

    @PostMapping
    @Operation(summary = "Create a type by name", description = ApiDescription.CREATE_TYPE_DESCRIPTION)
    public ResponseEntity<String> createType(@RequestBody TypeDTO typeDTO) {
        typeService.createType(typeDTO);
        return ResponseEntity.ok("Type created");
    }

    @DeleteMapping
    @Operation(summary = "Delete type by name", description = ApiDescription.DELETE_TYPE_DESCRIPTION)
    public ResponseEntity<String> deleteType(@RequestBody TypeDTO typeDTO) {
        typeService.deleteType(typeDTO);
        return ResponseEntity.ok("Type deleted successfully");
    }
}
