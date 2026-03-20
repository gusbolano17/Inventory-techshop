package com.ecommerce.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    
    
}
