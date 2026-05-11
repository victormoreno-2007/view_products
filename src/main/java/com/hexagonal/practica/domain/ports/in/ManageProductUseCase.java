package com.hexagonal.practica.domain.ports.in;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.hexagonal.practica.domain.model.product.Product;

public interface ManageProductUseCase {
    Product create(Product product);
    Page<Product> findAll(int page, int size);
    List<Product> findByUserId(UUID userId);
    Product findbyID(UUID id);
    Product update(UUID id, Product product, UUID userId);
    void deleteById (UUID id, UUID userId);
    Page<Product> findPaginatedByUserId(UUID userId, int page, int size);
    Product create(Product product, MultipartFile image);
}
