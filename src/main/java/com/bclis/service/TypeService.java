package com.bclis.service;

import com.bclis.dto.request.TypeDTO;
import com.bclis.persistence.entity.TypeEntity;
import com.bclis.persistence.repository.DocumentRepository;
import com.bclis.persistence.repository.TypeRepository;
import com.bclis.utils.exceptions.AlreadyExistsException;
import com.bclis.utils.exceptions.DependentResourceException;
import com.bclis.utils.exceptions.NotFoundException;
import com.bclis.utils.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TypeService {

    private final TypeRepository typeRepository;
    private final DocumentRepository documentRepository;
    public final JwtUtils jwtUtils;

    public List<String> getAllTypes() {
        List<String> authorities = jwtUtils.getAuthoritiesSecurityContext();
        List<TypeEntity> typeEntities;

        if (authorities.contains("ROLE_TECHNICAL")) {
            typeEntities = typeRepository.findByNameNot("Administrative");
        }
        else if (authorities.contains("ROLE_GENERIC")) {
            typeEntities = typeRepository.findByNameNot("Programming");
        }
        else {
            typeEntities = typeRepository.findAll();
        }

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
        TypeEntity typeEntity = typeRepository.findByName(typeDTO.getTypeName())
                .orElseThrow(() -> new NotFoundException("Type not found"));

        boolean existsTypeDependencies = documentRepository.existsByTypeId(typeEntity.getId());

        if (existsTypeDependencies) {
            throw new DependentResourceException("There are dependencies for this type that prevent this action");
        }

        typeRepository.delete(typeEntity);
    }
}
