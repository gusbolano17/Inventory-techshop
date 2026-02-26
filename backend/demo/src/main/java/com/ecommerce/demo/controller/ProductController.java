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
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.demo.model.dto.ProductDTO;
import com.ecommerce.demo.service.ProductService;


@RestController
@CrossOrigin(origins = { "*" })
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/listar")
    public ResponseEntity<?> listarProductos() throws Exception {
        return ResponseEntity.ok(productService.listarProductos());
    }

    @GetMapping("/obtener/{id}")
    public ResponseEntity<?> obtenerProducto(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok(productService.obtenerProductoId(id));
    }

    @GetMapping("/alerta-stock")
    public ResponseEntity<?> alertarStockBajo() throws Exception{
        return ResponseEntity.ok(productService.alertarStockBajo());
    }

    @PostMapping("/agregar")
    public ResponseEntity<?> agregarProducto(@RequestBody ProductDTO entity) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.crearProducto(entity));
    }

    @PutMapping("editar/{id}")
    public ResponseEntity<?> editarProducto(@PathVariable Long id, @RequestBody ProductDTO entity) throws Exception{
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.editarProducto(id, entity));
    }

    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(productService.eliminarProducto(id));
    }

}
