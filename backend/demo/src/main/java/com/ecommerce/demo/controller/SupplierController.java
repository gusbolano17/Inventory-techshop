package com.ecommerce.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.demo.model.dto.SupplierDto;
import com.ecommerce.demo.model.dto.SupplierFilterDto;
import com.ecommerce.demo.service.SupplierService;

@RestController
@CrossOrigin(origins = { "*" })
@RequestMapping("/api/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping("/listar")
    public ResponseEntity<?> listarProveedores() throws Exception {
        return ResponseEntity.ok(supplierService.listarProveedores());
    }

    @GetMapping("/filtrar")
    public ResponseEntity<?> filtrarProductos(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String taxId,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) throws Exception {
        SupplierFilterDto filter = new SupplierFilterDto(name, taxId, active);
        return ResponseEntity.ok(supplierService.filtrarProveedores(filter, page, size));
    }

    @PostMapping("/guardar")
    public ResponseEntity<?> guardarProveedor(@RequestBody SupplierDto body) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.crearProveedor(body));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarProveedor(@PathVariable Long id, @RequestBody SupplierDto body) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.editarProveedor(id, body));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarProveedor(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok().build();
    }
}
