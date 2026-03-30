package com.ecommerce.demo.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerce.demo.model.Category;
import com.ecommerce.demo.model.dto.CategoryDto;
import com.ecommerce.demo.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Electronics");
        testCategory.setActive(true);
        testCategory.setCreatedAt(new Timestamp(System.currentTimeMillis()));
    }

    @Test
    void listarCategorias_shouldReturnAllCategories() throws Exception {
        when(categoryRepository.findAll()).thenReturn(List.of(testCategory));

        List<Category> result = categoryService.listarCategorias();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getName());
    }

    @Test
    void listarCategorias_shouldReturnEmptyListWhenNoCategories() throws Exception {
        when(categoryRepository.findAll()).thenReturn(List.of());

        List<Category> result = categoryService.listarCategorias();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void obtenerCategoria_shouldReturnCategoryWhenExists() throws Exception {
        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.of(testCategory));

        Category result = categoryService.obtenerCategoria("Electronics");

        assertNotNull(result);
        assertEquals("Electronics", result.getName());
    }

    @Test
    void obtenerCategoria_shouldThrowExceptionWhenCategoryNotFound() throws Exception {
        when(categoryRepository.findByName("NonExistent")).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> categoryService.obtenerCategoria("NonExistent"));
    }

    @Test
    void obtenerCategoria_shouldThrowExceptionWhenCategoryInactive() throws Exception {
        testCategory.setActive(false);
        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.of(testCategory));

        assertThrows(Exception.class, () -> categoryService.obtenerCategoria("Electronics"));
    }

    @Test
    void crearCategoria_shouldCreateNewCategory() throws Exception {
        CategoryDto dto = new CategoryDto("New Category", "Description");
        when(categoryRepository.existsByName("New Category")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        Map<String, Object> result = categoryService.crearCategoria(dto);

        assertNotNull(result);
        assertEquals("Categoria creada exitosamente", result.get("message"));
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void crearCategoria_shouldThrowExceptionWhenCategoryExists() throws Exception {
        CategoryDto dto = new CategoryDto("Electronics", "Description");
        when(categoryRepository.existsByName("Electronics")).thenReturn(true);

        assertThrows(Exception.class, () -> categoryService.crearCategoria(dto));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void editarCategoria_shouldUpdateExistingCategory() throws Exception {
        CategoryDto dto = new CategoryDto("Updated Electronics", "Updated Description");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        Map<String, Object> result = categoryService.editarCategoria(1L, dto);

        assertNotNull(result);
        assertEquals("Categoria editada exitosamente", result.get("message"));
        assertEquals("Updated Electronics", testCategory.getName());
        verify(categoryRepository).save(testCategory);
    }

    @Test
    void editarCategoria_shouldThrowExceptionWhenCategoryNotFound() throws Exception {
        CategoryDto dto = new CategoryDto("Updated", "Description");
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> categoryService.editarCategoria(999L, dto));
    }

    @Test
    void editarCategoria_shouldThrowExceptionWhenCategoryInactive() throws Exception {
        CategoryDto dto = new CategoryDto("Updated", "Description");
        testCategory.setActive(false);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        assertThrows(Exception.class, () -> categoryService.editarCategoria(1L, dto));
    }

    @Test
    void eliminarCategoria_shouldDeleteCategory() throws Exception {
        Category categoryToDelete = new Category();
        categoryToDelete.setId(1L);
        categoryToDelete.setName("ToDelete");
        categoryToDelete.setActive(true);
        
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryToDelete));

        Map<String, Object> result = categoryService.eliminarCategoria(1L);

        assertNotNull(result);
        assertEquals("Categoria eliminada exitosamente", result.get("message"));
        verify(categoryRepository).delete(categoryToDelete);
    }

    @Test
    void eliminarCategoria_shouldThrowExceptionWhenCategoryNotFound() throws Exception {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> categoryService.eliminarCategoria(999L));
    }

    @Test
    void eliminarCategoria_shouldThrowExceptionWhenCategoryInactive() throws Exception {
        Category inactiveCategory = new Category();
        inactiveCategory.setId(2L);
        inactiveCategory.setName("Inactive");
        inactiveCategory.setActive(false);
        
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(inactiveCategory));

        assertThrows(Exception.class, () -> categoryService.eliminarCategoria(2L));
    }
}
