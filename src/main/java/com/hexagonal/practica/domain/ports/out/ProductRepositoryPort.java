package com.hexagonal.practica.domain.ports.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.hexagonal.practica.domain.model.product.Product;

public interface ProductRepositoryPort {

    Product save(Product product);
    Optional<Product> findById(UUID id);
    List<Product> findByUserId(UUID userId);
    List<Product> findAll();
    void deleteById(UUID id);
} 