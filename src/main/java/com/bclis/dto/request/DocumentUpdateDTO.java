package com.bclis.dto.request;

import com.bclis.persistence.entity.DocumentEntity.DocumentState;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentUpdateDTO {

    @Schema(description = "Updated name of the document",
            example = "Updated Project Proposal",
            type = "string",
            minLength = 3,
            maxLength = 100)
    private String name;

    @Schema(description = "Updated description of the document",
            example = "Updated project proposal details",
            type = "string",
            maxLength = 500)
    private String description;

    @Schema(description = "Updated state of the document",
            example = "PUBLISHED",
            type = "string",
            allowableValues = {"DRAFT", "PUBLISHED", "ARCHIVED"})
    private DocumentState state;

    @Schema(description = "Updated categories associated with the document",
            example = "[\"New Category\", \"New Category\"]",
            type = "array")
    @Size(min = 1, message = "At least one category is required")
    private List<String> categories;
}
