package com.bclis.service;

import com.bclis.persistence.entity.TypeEntity;
import com.bclis.persistence.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TypeService {

    private final TypeRepository typeRepository;

    public List<String> getAllTypes() {
        List<TypeEntity> typeEntities = typeRepository.findAll();
        List<String> types = new ArrayList<>();
        typeEntities.forEach(t -> types.add(t.getName()));
        return types;
    }
}
