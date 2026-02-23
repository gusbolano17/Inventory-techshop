package com.ecommerce.demo.model.dto;

public record ProductDTO(String name, String description, String categoria, String sku, Double salePrice, Double costPrice, Long stock) {
    
}
