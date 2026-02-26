package com.ecommerce.demo.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "brands", indexes = {
        @Index(name = "idx_brand_name", columnList = "name"),
})
public class Brand {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String name;
        private String description;
        private String originCountry;
        private Boolean active = true;
        @CreationTimestamp
        private LocalDateTime createdAt;

}
