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
import static org.mockito.ArgumentMatchers.argThat;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerce.demo.model.Supplier;
import com.ecommerce.demo.model.dto.SupplierDto;
import com.ecommerce.demo.repository.SupplierRepository;

@ExtendWith(MockitoExtension.class)
class SupplierServiceTest {

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierService supplierService;

    private Supplier testSupplier;

    @BeforeEach
    void setUp() {
        testSupplier = new Supplier();
        testSupplier.setId(1L);
        testSupplier.setName("Test Supplier");
        testSupplier.setTaxId("1234567-8");
        testSupplier.setEmail("test@supplier.com");
        testSupplier.setPhone("+50212345678");
        testSupplier.setAddress("Test Address");
        testSupplier.setContactName("Contact Person");
        testSupplier.setActive(true);
        testSupplier.setCreatedAt(new Timestamp(System.currentTimeMillis()));
    }

    @Test
    void listarProveedores_shouldReturnAllSuppliers() throws Exception {
        when(supplierRepository.findAll()).thenReturn(List.of(testSupplier));

        List<Supplier> result = supplierService.listarProveedores();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Supplier", result.get(0).getName());
    }

    @Test
    void listarProveedores_shouldReturnEmptyListWhenNoSuppliers() throws Exception {
        when(supplierRepository.findAll()).thenReturn(List.of());

        List<Supplier> result = supplierService.listarProveedores();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void obtenerProveedor_shouldReturnSupplierWhenExists() throws Exception {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(testSupplier));

        Supplier result = supplierService.obtenerProveedorId(1L);

        assertNotNull(result);
        assertEquals("Test Supplier", result.getName());
    }

    @Test
    void obtenerProveedor_shouldThrowExceptionWhenSupplierNotFound() throws Exception {
        when(supplierRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> supplierService.obtenerProveedorId(999L));
    }

    @Test
    void crearProveedor_shouldCreateNewSupplier() throws Exception {
        SupplierDto dto = new SupplierDto("New Supplier", "9999999-9", "new@supplier.com", "+50299999999",
                "New Address", "New Contact");
        when(supplierRepository.save(any(Supplier.class))).thenReturn(testSupplier);

        Map<String, Object> result = supplierService.crearProveedor(dto);

        assertNotNull(result);
        assertEquals("Proveedor creado exitosamente", result.get("msg"));
        verify(supplierRepository).save(any(Supplier.class));
    }

    @Test
    void crearProveedor_shouldSetActiveToTrue() throws Exception {
        SupplierDto dto = new SupplierDto("New", "999", "a@b.com", "123", "addr", "contact");
        when(supplierRepository.save(any(Supplier.class))).thenAnswer(invocation -> invocation.getArgument(0));

        supplierService.crearProveedor(dto);

        verify(supplierRepository).save(argThat(supplier -> supplier.getActive() == true));
    }

    @Test
    void editarProveedor_shouldUpdateExistingSupplier() throws Exception {
        SupplierDto dto = new SupplierDto("Updated Supplier", "1111111-1", "updated@supplier.com", "+50211111111",
                "Updated Address", "Updated Contact");
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(testSupplier));
        when(supplierRepository.save(any(Supplier.class))).thenReturn(testSupplier);

        Map<String, Object> result = supplierService.editarProveedor(1L, dto);

        assertNotNull(result);
        assertEquals("Proveedor actualizado exitosamente", result.get("msg"));
        assertEquals("Updated Supplier", testSupplier.getName());
        verify(supplierRepository).save(testSupplier);
    }

    @Test
    void editarProveedor_shouldThrowExceptionWhenSupplierNotFound() throws Exception {
        SupplierDto dto = new SupplierDto("Updated", "123", "test@test.com", "123", "addr", "contact");
        when(supplierRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> supplierService.editarProveedor(999L, dto));
    }

    @Test
    void eliminarProveedor_shouldDeleteSupplier() throws Exception {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(testSupplier));

        Map<String, Object> result = supplierService.eliminarProveedor(1L);

        assertNotNull(result);
        assertEquals("Proveedor eliminado exitosamente", result.get("msg"));
        verify(supplierRepository).delete(testSupplier);
    }

    @Test
    void eliminarProveedor_shouldThrowExceptionWhenSupplierNotFound() throws Exception {
        when(supplierRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> supplierService.eliminarProveedor(999L));
    }


}
