package com.ecommerce.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerce.demo.model.Brand;
import com.ecommerce.demo.repository.BrandRepository;

@ExtendWith(MockitoExtension.class)
class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandService brandService;

    private Brand testBrand;

    @BeforeEach
    void setUp() {
        testBrand = new Brand();
        testBrand.setId(1L);
        testBrand.setName("Samsung");
        testBrand.setDescription("Korean electronics brand");
        testBrand.setOriginCountry("South Korea");
        testBrand.setActive(true);
    }

    @Test
    void listarMarcas_shouldReturnAllBrands() throws Exception {
        when(brandRepository.findAll()).thenReturn(List.of(testBrand));

        List<Brand> result = brandService.listarMarcas();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Samsung", result.get(0).getName());
    }

    @Test
    void listarMarcas_shouldReturnEmptyListWhenNoBrands() throws Exception {
        when(brandRepository.findAll()).thenReturn(List.of());

        List<Brand> result = brandService.listarMarcas();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void obtenerMarcaNombre_shouldReturnBrandWhenExists() throws Exception {
        when(brandRepository.findByName("Samsung")).thenReturn(Optional.of(testBrand));

        Brand result = brandService.obtenerMarcaNombre("Samsung");

        assertNotNull(result);
        assertEquals("Samsung", result.getName());
    }

    @Test
    void obtenerMarcaNombre_shouldThrowExceptionWhenBrandNotFound() throws Exception {
        when(brandRepository.findByName("NonExistent")).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> brandService.obtenerMarcaNombre("NonExistent"));
    }

    @Test
    void obtenerMarcaNombre_shouldThrowExceptionWhenBrandInactive() throws Exception {
        testBrand.setActive(false);
        when(brandRepository.findByName("Samsung")).thenReturn(Optional.of(testBrand));

        assertThrows(Exception.class, () -> brandService.obtenerMarcaNombre("Samsung"));
    }
}
