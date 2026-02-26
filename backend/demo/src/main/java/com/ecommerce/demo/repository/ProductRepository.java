package com.ecommerce.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.demo.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
    
    @Query("SELECT p FROM Product p WHERE p.stock < p.minStock")
    List<Product> productosStockBajo() throws Exception;

}
