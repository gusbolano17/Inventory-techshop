package com.ecommerce.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.demo.model.dto.CategoryDto;
import com.ecommerce.demo.service.CategoryService;

@RestController
@CrossOrigin(origins = { "*" })
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listar")
    public ResponseEntity<?> listarCategorias() throws Exception {
        return ResponseEntity.ok(categoryService.listarCategorias());
    }

    @PostMapping("/guardar")
    public ResponseEntity<?> guardarCategoria(@RequestBody CategoryDto body) {
        try {
            return ResponseEntity.ok(categoryService.crearCategoria(body));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarCategoria(@PathVariable Long id, @RequestBody CategoryDto body) {
        try {
            return ResponseEntity.ok(categoryService.editarCategoria(id, body));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(categoryService.eliminarCategoria(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
