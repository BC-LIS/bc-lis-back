package com.bclis.dto.request;

import com.bclis.persistence.entity.DocumentEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentCreateDTO {
    private String name;
    private String description;
    private MultipartFile file;
    private DocumentEntity.DocumentState state;
    private String typeName;
    private String username;
}

