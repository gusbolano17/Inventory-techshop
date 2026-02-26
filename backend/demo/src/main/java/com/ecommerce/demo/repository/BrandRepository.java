package com.ecommerce.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.demo.model.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long>{

    Optional<Brand> findByName(String name) throws Exception;
    
}
