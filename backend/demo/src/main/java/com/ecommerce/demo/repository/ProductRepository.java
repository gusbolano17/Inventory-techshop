package com.ecommerce.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.demo.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
    
}
