package com.ecommerce.demo.utils.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.ecommerce.demo.model.Supplier;

public class SupplierSpecification {

    public static Specification<Supplier> byName(String name) {
        return ((root, query, cb) -> {
            if (name == null || name.isBlank())
                return null;
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        });
    }

    public static Specification<Supplier> byTaxId(String taxId) {
        return ((root, query, cb) -> {
            if (taxId == null || taxId.isBlank())
                return null;
            return cb.like(cb.lower(root.get("taxId")), "%" + taxId.toLowerCase() + "%");
        });
    }

    public static Specification<Supplier> byActive(Boolean active) {
        return ((root, query, cb) -> {
            if (active == null)
                return null;
            return cb.equal(root.get("active"), active);
        });

    }

}
