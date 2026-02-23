package com.ecommerce.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.demo.model.Category;
import com.ecommerce.demo.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> listarCategorias() throws Exception{
        return categoryRepository.findAll();
    }

    public Category obtenerCategoria(String nombre) throws Exception{
        return categoryRepository.findByName(nombre)
        .filter(c -> c.isActive())
        .orElseThrow(() -> new Exception("La categoria no existe o no esta activa"));
    }
    
    
}
