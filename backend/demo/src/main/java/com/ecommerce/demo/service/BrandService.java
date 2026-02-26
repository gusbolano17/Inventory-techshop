package com.ecommerce.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.demo.model.Brand;
import com.ecommerce.demo.repository.BrandRepository;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> listarMarcas() throws Exception {
        return brandRepository.findAll();
    }

    public Brand obtenerMarcaNombre(String nombre) throws Exception {
        return brandRepository.findByName(nombre)
                .filter(c -> c.getActive())
                .orElseThrow(() -> new Exception("La marca no existe o no esta activa"));
    }

}
