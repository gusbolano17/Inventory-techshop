package com.ecommerce.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.ecommerce.demo.model.Brand;
import com.ecommerce.demo.model.Category;
import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.model.dto.ProductDTO;
import com.ecommerce.demo.model.dto.ProductFilterDTO;
import com.ecommerce.demo.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BrandService brandService;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private Category testCategory;
    private Brand testBrand;
    private ProductDTO testProductDTO;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Electronics");
        testCategory.setActive(true);

        testBrand = new Brand();
        testBrand.setId(1L);
        testBrand.setName("Samsung");
        testBrand.setActive(true);

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Galaxy S24");
        testProduct.setSku("SKU001");
        testProduct.setStock(50L);
        testProduct.setMinStock(10L);
        testProduct.setCostPrice(new BigDecimal("500.00"));
        testProduct.setSalePrice(new BigDecimal("800.00"));
        testProduct.setDescription("Smartphone");
        testProduct.setCategoryId(testCategory);
        testProduct.setBrandId(testBrand);

        testProductDTO = new ProductDTO(
            "Galaxy S24", "Samsung", "Smartphone", "Electronics",
            "SKU001", 10L, 800.0, 500.0
        );
    }

    @Test
    void listarProductos_shouldReturnAllProducts() throws Exception {
        when(productRepository.findAll()).thenReturn(List.of(testProduct));

        List<Product> result = productService.listarProductos();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Galaxy S24", result.get(0).getName());
    }

    @Test
    void obtenerProductoId_shouldReturnProduct() throws Exception {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        Product result = productService.obtenerProductoId(1L);

        assertNotNull(result);
        assertEquals("Galaxy S24", result.getName());
    }

    @Test
    void obtenerProductoId_shouldThrowExceptionWhenNotFound() throws Exception {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> productService.obtenerProductoId(999L));
        assertEquals("Producto no encontrado", exception.getMessage());
    }

    @Test
    void filtrarProductos_shouldReturnPagedProducts() throws Exception {
        ProductFilterDTO filter = new ProductFilterDTO("Galaxy", null, null, null);
        Page<Product> pagedProducts = new PageImpl<>(List.of(testProduct));
        
        when(productRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pagedProducts);

        Page<Product> result = productService.filtrarProductos(filter, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void crearProducto_shouldCreateProductSuccessfully() throws Exception {
        when(categoryService.obtenerCategoria("Electronics")).thenReturn(testCategory);
        when(brandService.obtenerMarcaNombre("Samsung")).thenReturn(testBrand);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        Map<String, Object> result = productService.crearProducto(testProductDTO);

        assertEquals("success", result.get("severity"));
        assertEquals("Producto creado con exito", result.get("msg"));
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void editarProducto_shouldUpdateProductSuccessfully() throws Exception {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(categoryService.obtenerCategoria("Electronics")).thenReturn(testCategory);
        when(brandService.obtenerMarcaNombre("Samsung")).thenReturn(testBrand);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        Map<String, Object> result = productService.editarProducto(1L, testProductDTO);

        assertEquals("success", result.get("severity"));
        assertEquals("Producto editado con exito", result.get("msg"));
    }

    @Test
    void eliminarProducto_shouldDeleteProductSuccessfully() throws Exception {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        doNothing().when(productRepository).delete(testProduct);

        Map<String, Object> result = productService.eliminarProducto(1L);

        assertEquals("success", result.get("severity"));
        assertEquals("Producto eliminado con exito", result.get("msg"));
        verify(productRepository).delete(testProduct);
    }

    @Test
    void eliminarProducto_shouldReturnErrorWhenNotFound() throws Exception {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        Map<String, Object> result = productService.eliminarProducto(999L);

        assertEquals("error", result.get("severity"));
        assertEquals("Producto no encontrado", result.get("msg"));
    }

    @Test
    void alertarStockBajo_shouldReturnLowStockProducts() throws Exception {
        when(productRepository.productosStockBajo()).thenReturn(List.of(testProduct));

        Map<String, Object> result = productService.alertarStockBajo();

        assertEquals("warn", result.get("severity"));
        assertNotNull(result.get("productos"));
    }
}
