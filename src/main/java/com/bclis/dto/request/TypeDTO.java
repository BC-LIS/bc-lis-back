package com.bclis.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TypeDTO {
    @Schema(description = "Name of the document type",
            example = "Report",
            type = "string",
            minLength = 3,
            maxLength = 50)
    private String typeName;
}
