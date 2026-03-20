package com.ecommerce.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.demo.service.BrandService;


@RestController
@CrossOrigin(origins = { "*" })
@RequestMapping("/api/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("/listar")
    public ResponseEntity<?> listarMarcas() throws Exception {
        return ResponseEntity.ok(brandService.listarMarcas());
    }
    
    
}
