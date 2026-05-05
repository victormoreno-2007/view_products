package com.hexagonal.practica.domain.ports.in;

import java.util.List;
import java.util.UUID;

import com.hexagonal.practica.domain.model.product.Product;

public interface ManageProductUseCase {
    Product create(Product product);
    List<Product> findAll();
    List<Product> findByUserId(UUID userId);
    Product findbyID(UUID id);
    Product update(UUID id, Product product);
    void deleteById (UUID id);
}
