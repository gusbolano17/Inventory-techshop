package com.ecommerce.demo.model.dto;

public record SupplierDto(
    String name,
    String taxId,
    String email,
    String phone,
    String address,
    String contactName
) {}
