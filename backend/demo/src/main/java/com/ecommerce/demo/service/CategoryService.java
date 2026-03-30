package com.ecommerce.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.demo.model.Category;
import com.ecommerce.demo.model.dto.CategoryDto;
import com.ecommerce.demo.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> listarCategorias() throws Exception {
        return categoryRepository.findAll();
    }

    public Category obtenerCategoria(String nombre) throws Exception {
        return categoryRepository.findByName(nombre)
                .filter(c -> c.isActive())
                .orElseThrow(() -> new Exception("La categoria no existe o no esta activa"));
    }

    public Category obtenerCategoriaId(Long id) throws Exception {
        return categoryRepository.findById(id)
                .filter(c -> c.isActive())
                .orElseThrow(() -> new Exception("La categoria no existe o no esta activa"));
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> crearCategoria(CategoryDto body) throws Exception {
        if (categoryRepository.existsByName(body.name())) {
            throw new Exception("La categoria ya existe");
        }
        Category category = new Category();
        category.setName(body.name());
        category.setDescription(body.description());
        category.setActive(true);
        categoryRepository.save(category);
        return Map.of("message", "Categoria creada exitosamente");
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> editarCategoria(Long id, CategoryDto body) throws Exception {

        Category categoryExist = obtenerCategoriaId(id);
        categoryExist.setName(body.name());
        categoryExist.setDescription(body.description());
        categoryRepository.save(categoryExist);

        return Map.of("message", "Categoria editada exitosamente");

    }

    public Map<String, Object> eliminarCategoria(Long id) throws Exception {
        Category categoryExist = obtenerCategoriaId(id);

        categoryRepository.delete(categoryExist);

        return Map.of("message", "Categoria eliminada exitosamente");
    }

}
