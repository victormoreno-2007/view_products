package com.hexagonal.practica.domain.ports.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hexagonal.practica.domain.model.product.Product;

public interface ProductRepositoryPort {

    Product save(Product product);
    Optional<Product> findById(UUID id);
    List<Product> findByUserId(UUID userId);
    Page<Product> findAll(int page, int size);
    void deleteById(UUID id);
} 