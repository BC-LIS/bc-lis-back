package com.bclis.service;

import com.bclis.dto.request.CategoryDTO;
import com.bclis.persistence.entity.CategoryEntity;
import com.bclis.persistence.repository.CategoryRepository;
import com.bclis.persistence.repository.DocumentCategoryRepository;
import com.bclis.utils.exceptions.AlreadyExistsException;
import com.bclis.utils.exceptions.NotFoundException;
import com.bclis.utils.exceptions.DependentResourceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private DocumentCategoryRepository documentCategoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    // Configuración inicial antes de cada prueba
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Inicializa los mocks
    }

    @Test
    void testGetAllCategories() {
        // Mock de la respuesta de CategoryRepository
        CategoryEntity category1 = new CategoryEntity();
        category1.setName("Category1");

        CategoryEntity category2 = new CategoryEntity();
        category2.setName("Category2");

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        // Llamada al método de servicio
        List<String> categories = categoryService.getAllCategories();

        // Aserciones
        assertEquals(2, categories.size());
        assertTrue(categories.contains("Category1"));
        assertTrue(categories.contains("Category2"));
    }

    @Test
    void testCreateCategory_whenCategoryDoesNotExist() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName("NewCategory");

        // Mock para verificar si la categoría ya existe
        when(categoryRepository.existsByName("NewCategory")).thenReturn(false);

        // Llamada al método de servicio
        categoryService.createCategory(categoryDTO);

        // Verifica que se haya guardado la categoría
        verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
    }

    @Test
    void testCreateCategory_whenCategoryExists() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName("ExistingCategory");

        // Mock para verificar si la categoría ya existe
        when(categoryRepository.existsByName("ExistingCategory")).thenReturn(true);

        // Verifica que la excepción sea lanzada
        assertThrows(AlreadyExistsException.class, () -> categoryService.createCategory(categoryDTO));

        // Verifica que no se haya guardado la categoría
        verify(categoryRepository, never()).save(any(CategoryEntity.class));
    }

    @Test
    void testDeleteCategoryByName_whenCategoryNotFound() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName("NonExistentCategory");

        // Mock para devolver un Optional vacío (categoría no encontrada)
        when(categoryRepository.findByName("NonExistentCategory")).thenReturn(Optional.empty());

        // Verifica que la excepción sea lanzada
        assertThrows(NotFoundException.class, () -> categoryService.deleteCategoryByName(categoryDTO));

        // No se debe eliminar ninguna categoría
        verify(categoryRepository, never()).delete(any(CategoryEntity.class));
    }

    @Test
    void testDeleteCategoryByName_whenCategoryHasDependencies() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName("CategoryWithDependencies");

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName("CategoryWithDependencies");

        // Mock para devolver la categoría
        when(categoryRepository.findByName("CategoryWithDependencies")).thenReturn(Optional.of(categoryEntity));
        // Mock para verificar si hay dependencias
        when(documentCategoryRepository.existsByCategoryId(categoryEntity.getId())).thenReturn(true);

        // Verifica que la excepción sea lanzada
        assertThrows(DependentResourceException.class, () -> categoryService.deleteCategoryByName(categoryDTO));

        // No se debe eliminar ninguna categoría
        verify(categoryRepository, never()).delete(any(CategoryEntity.class));
    }

    @Test
    void testDeleteCategoryByName_whenCategoryDeleted() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName("CategoryWithoutDependencies");

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName("CategoryWithoutDependencies");

        // Mock para devolver la categoría
        when(categoryRepository.findByName("CategoryWithoutDependencies")).thenReturn(Optional.of(categoryEntity));
        // Mock para verificar que no hay dependencias
        when(documentCategoryRepository.existsByCategoryId(categoryEntity.getId())).thenReturn(false);

        categoryService.deleteCategoryByName(categoryDTO);

        // Verifica que la categoría haya sido eliminada
        verify(categoryRepository, times(1)).delete(categoryEntity);
    }
}
