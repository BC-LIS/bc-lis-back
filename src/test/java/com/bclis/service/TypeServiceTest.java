package com.bclis.service;
import com.bclis.service.TypeService;
import com.bclis.dto.request.TypeDTO;
import com.bclis.persistence.entity.TypeEntity;
import com.bclis.persistence.repository.TypeRepository;
import com.bclis.persistence.repository.DocumentRepository;
import com.bclis.utils.exceptions.AlreadyExistsException;
import com.bclis.utils.exceptions.DependentResourceException;
import com.bclis.utils.exceptions.NotFoundException;
import com.bclis.utils.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Asegura que los mocks se inicialicen correctamente
class TypeServiceTest {

    @Mock
    private TypeRepository typeRepository;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private TypeService typeService;

    @Test
    void testGetAllTypesWithRoleTechnical() {
        // Simulando el comportamiento del JWT
        when(jwtUtils.getAuthoritiesSecurityContext()).thenReturn(Arrays.asList("ROLE_TECHNICAL"));

        // Simulando el repositorio
        TypeEntity type1 = new TypeEntity(1L, "Programming");
        TypeEntity type2 = new TypeEntity(2L, "Design");
        when(typeRepository.findByNameNot("Administrative")).thenReturn(Arrays.asList(type1, type2));

        // Ejecutando el método
        List<String> types = typeService.getAllTypes();

        // Verificando el resultado
        assertEquals(2, types.size());
        assertTrue(types.contains("Programming"));
        assertTrue(types.contains("Design"));
    }

    @Test
    void testCreateTypeWhenTypeExists() {
        TypeDTO typeDTO = new TypeDTO();
        typeDTO.setTypeName("Programming");

        // Simulando que el tipo ya existe en el repositorio
        when(typeRepository.existsByName("Programming")).thenReturn(true);

        // Verificando que se lance la excepción
        assertThrows(AlreadyExistsException.class, () -> typeService.createType(typeDTO));
    }

    @Test
    void testCreateTypeWhenTypeDoesNotExist() {
        TypeDTO typeDTO = new TypeDTO();
        typeDTO.setTypeName("Design");

        // Simulando que el tipo no existe
        when(typeRepository.existsByName("Design")).thenReturn(false);

        // Simulando el guardado en el repositorio
        typeService.createType(typeDTO);

        // Verificando que se haya guardado correctamente
        verify(typeRepository, times(1)).save(any());
    }

    @Test
    void testDeleteTypeWhenNotFound() {
        TypeDTO typeDTO = new TypeDTO();
        typeDTO.setTypeName("NonExistentType");

        // Simulando que el tipo no existe en el repositorio
        when(typeRepository.findByName("NonExistentType")).thenReturn(java.util.Optional.empty());

        // Verificando que se lance la excepción NotFoundException
        assertThrows(NotFoundException.class, () -> typeService.deleteType(typeDTO));
    }

    @Test
    void testDeleteTypeWhenDependenciesExist() {
        TypeDTO typeDTO = new TypeDTO();
        typeDTO.setTypeName("Programming");

        TypeEntity typeEntity = new TypeEntity(1L, "Programming");

        // Simulando que el tipo existe
        when(typeRepository.findByName("Programming")).thenReturn(java.util.Optional.of(typeEntity));

        // Simulando que existen dependencias para el tipo
        when(documentRepository.existsByTypeId(typeEntity.getId())).thenReturn(true);

        // Verificando que se lance la excepción DependentResourceException
        assertThrows(DependentResourceException.class, () -> typeService.deleteType(typeDTO));
    }

    @Test
    void testDeleteTypeWhenNoDependencies() {
        TypeDTO typeDTO = new TypeDTO();
        typeDTO.setTypeName("Design");

        TypeEntity typeEntity = new TypeEntity(2L, "Design");

        // Simulando que el tipo existe
        when(typeRepository.findByName("Design")).thenReturn(java.util.Optional.of(typeEntity));

        // Simulando que no existen dependencias
        when(documentRepository.existsByTypeId(typeEntity.getId())).thenReturn(false);

        // Ejecutando el método de eliminación
        typeService.deleteType(typeDTO);

        // Verificando que se haya eliminado el tipo
        verify(typeRepository, times(1)).delete(typeEntity);
    }
}
