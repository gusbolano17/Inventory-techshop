package com.ecommerce.demo.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.demo.model.Supplier;
import com.ecommerce.demo.model.dto.SupplierDto;
import com.ecommerce.demo.model.dto.SupplierFilterDto;
import com.ecommerce.demo.repository.SupplierRepository;
import com.ecommerce.demo.utils.specifications.SupplierSpecification;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    public List<Supplier> listarProveedores() throws Exception {
        return supplierRepository.findAll();
    }

    public Supplier obtenerProveedorId(Long id) throws Exception {
        return supplierRepository.findById(id).orElseThrow(
                () -> new Exception("Proveedor no encontrado"));
    }

    public Page<Supplier> filtrarProveedores(SupplierFilterDto filter, Integer page, Integer size) throws Exception {

        Pageable pageable = PageRequest.of(page, size);

        Specification<Supplier> spec = Stream.of(
                SupplierSpecification.byName(filter.name()),
                SupplierSpecification.byTaxId(filter.taxId()),
                SupplierSpecification.byActive(filter.active())
            ).filter(Objects::nonNull)
            .reduce(Specification::and)
            .orElse(null);

        return supplierRepository.findAll(spec, pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> crearProveedor(SupplierDto body) throws Exception {
        
        Supplier supplier = new Supplier();
        supplier.setName(body.name());
        supplier.setTaxId(body.taxId());
        supplier.setActive(true);
        supplier.setAddress(body.address());
        supplier.setPhone(body.phone());
        supplier.setEmail(body.email());
        supplier.setContactName(body.contactName());
        supplierRepository.save(supplier);

        return Map.of("msg", "Proveedor creado exitosamente");
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> editarProveedor(Long id, SupplierDto body) throws Exception {

        Supplier supplierExist = obtenerProveedorId(id);
        supplierExist.setName(body.name());
        supplierExist.setTaxId(body.taxId());
        supplierExist.setActive(true);
        supplierExist.setAddress(body.address());
        supplierExist.setPhone(body.phone());
        supplierExist.setEmail(body.email());
        supplierExist.setContactName(body.contactName());
        supplierRepository.save(supplierExist);

        return Map.of("msg", "Proveedor actualizado exitosamente");
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> eliminarProveedor(Long id) throws Exception {
        // TODO: implementar logica
        return Map.of();
    }
}
