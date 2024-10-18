package com.bclis.service;

import com.bclis.dto.request.TypeDTO;
import com.bclis.persistence.entity.TypeEntity;
import com.bclis.persistence.repository.DocumentRepository;
import com.bclis.persistence.repository.TypeRepository;
import com.bclis.utils.exceptions.AlreadyExistsException;
import com.bclis.utils.exceptions.DependentResourceException;
import com.bclis.utils.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TypeService {

    private final TypeRepository typeRepository;
    private final DocumentRepository documentRepository;

    public List<String> getAllTypes() {
        List<TypeEntity> typeEntities = typeRepository.findAll();
        List<String> types = new ArrayList<>();
        typeEntities.forEach(t -> types.add(t.getName()));
        return types;
    }

    public void createType(TypeDTO typeDTO) {
        String typeName = typeDTO.getTypeName();
        boolean existsType = typeRepository.existsByName(typeName);

        if (existsType) {
            throw new AlreadyExistsException("Type already exists");
        }

        TypeEntity typeEntity = new TypeEntity();
        typeEntity.setName(typeName);
        typeRepository.save(typeEntity);
    }

    public void deleteType(TypeDTO typeDTO) {
        TypeEntity typeEntity = typeRepository.findByName(typeDTO.getTypeName());

        if (typeEntity == null) {
            throw new NotFoundException("Type not found");
        }

        boolean existsTypeDependecies = documentRepository.existsByTypeId(typeEntity.getId());

        if (existsTypeDependecies) {
            throw new DependentResourceException("There are dependencies for this type that prevent this action");
        }

        typeRepository.delete(typeEntity);
    }
}
