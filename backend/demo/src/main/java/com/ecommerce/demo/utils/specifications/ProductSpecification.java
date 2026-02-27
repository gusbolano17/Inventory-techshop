package com.ecommerce.demo.utils.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.ecommerce.demo.model.Brand;
import com.ecommerce.demo.model.Category;
import com.ecommerce.demo.model.Product;

import jakarta.persistence.criteria.Join;

public class ProductSpecification {

    public static Specification<Product> byName(String name) {
        return ((root, query, cb) -> {
            if (name == null || name.isBlank())
                return null;
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        });
    }

    public static Specification<Product> bySku(String sku) {
        return ((root, query, cb) -> {
            if (sku == null || sku.isBlank())
                return null;
            return cb.like(cb.lower(root.get("sku")), "%" + sku.toLowerCase() + "%");
        });
    }

    public static Specification<Product> byCategoryName(String categoryName) {
        return ((root, query, cb) -> {
            if (categoryName == null || categoryName.isBlank())
                return null;
            query.distinct(true);
            Join<Product, Category> categoryJoin = root.join("categoryId");
            return cb.like(cb.lower(categoryJoin.get("name")), "%" + categoryName.toLowerCase() + "%");
        });
    }

    public static Specification<Product> bybrandName(String brandName) {
        return ((root, query, cb) -> {
            if (brandName == null || brandName.isBlank())
                return null;
            query.distinct(true);
            Join<Product, Brand> brandJoin = root.join("brandId");
            return cb.like(cb.lower(brandJoin.get("name")), "%" + brandName.toLowerCase() + "%");
        });
    }

}
