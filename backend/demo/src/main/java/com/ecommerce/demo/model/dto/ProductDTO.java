package com.ecommerce.demo.model.dto;

public record ProductDTO(String name, String brand,String description, String categoria, String sku, Long minStock, Double salePrice, Double costPrice) {
    
}
