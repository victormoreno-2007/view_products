package com.hexagonal.practica.infrastructure.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexagonal.practica.infrastructure.adapters.out.persistence.entities.ProductEntity;

public interface SpringDataProductRepository extends JpaRepository<ProductEntity, UUID>{
    List<ProductEntity> findByUserId(UUID userId);
}
