package com.ecommerce.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.demo.model.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>{
    
}
