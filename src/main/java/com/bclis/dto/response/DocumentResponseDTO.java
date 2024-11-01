package com.bclis.dto.response;

import com.bclis.persistence.entity.CategoryEntity;
import com.bclis.persistence.entity.DocumentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    private TypeResponseDTO type;
    private UserResponseDTO user;
    private List<CategoryResponseDTO> categories;
}

