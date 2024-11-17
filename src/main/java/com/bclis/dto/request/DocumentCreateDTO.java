package com.bclis.dto.request;

import com.bclis.persistence.entity.DocumentEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentCreateDTO {
    @Schema(description = "Name of the document",
            example = "Project Proposal",
            type = "string",
            required = true,
            minLength = 3,
            maxLength = 100)
    private String name;


    @Schema(description = "Description of the document",
            example = "Detailed project proposal document for Q1 2024",
            type = "string",
            maxLength = 500)
    private String description;


    @Schema(description = "File to be uploaded, representing the document content",
            type = "string",
            format = "binary",
            required = true)
    private MultipartFile file;


    @Schema(description = "Current state of the document",
            example = "DRAFT",
            type = "string",
            allowableValues = {"DRAFT", "PUBLISHED", "ARCHIVED"},
            required = true)
    private DocumentEntity.DocumentState state;


    @Schema(description = "Type of the document (Programming, Administrative or Both)",
            example = "Programming",

            type = "string",
            required = true,
            minLength = 3,
            maxLength = 50)
    private String typeName;


    @Schema(description = "Username of the creator of the document",
            example = "jdoe",
            type = "string",
            required = true)
    private String username;

    /**
     * List of categories associated with the document, allowing categorization and tagging.
     */
    @Schema(description = "Categories associated with the document, must contain at least one category",
            example = "[\"Ansible\", \"Docker\", \"Projects\"]",
            type = "array")
    @Size(min = 1, message = "At least one category is required")
    private List<String> categories;
}

