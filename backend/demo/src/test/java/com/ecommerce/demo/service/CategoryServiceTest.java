package com.ecommerce.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerce.demo.model.Category;
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
}
