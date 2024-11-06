package com.bclis.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {
    @Schema(description = "Name of the document category",
            example = "Finance",
            type = "string",
            required = true,
            minLength = 3,
            maxLength = 50)
    private String categoryName;
}
