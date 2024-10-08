package com.bclis.configuration;

import com.bclis.dto.request.DocumentCreateDTO;
import com.bclis.persistence.entity.DocumentEntity;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Configurar el mapeo específico para DocumentCreateDTO y DocumentEntity
        modelMapper.createTypeMap(DocumentCreateDTO.class, DocumentEntity.class)
                .addMappings(mapper -> mapper.map(DocumentCreateDTO::getState, DocumentEntity::setState));

        return modelMapper;
    }
}

