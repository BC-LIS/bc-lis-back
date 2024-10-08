package com.bclis.dto.response;

import com.bclis.persistence.entity.DocumentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String objectName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private DocumentEntity.DocumentState state;
}

