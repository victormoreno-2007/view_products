package com.hexagonal.practica.infrastructure.adapters.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.hexagonal.practica.domain.model.product.Product;
import com.hexagonal.practica.domain.ports.out.ProductRepositoryPort;
import com.hexagonal.practica.infrastructure.adapters.out.persistence.entities.ProductEntity;
import com.hexagonal.practica.infrastructure.adapters.out.persistence.mappers.ProductMapper;
import com.hexagonal.practica.infrastructure.repositories.SpringDataProductRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductRepositoryPort{

    private final SpringDataProductRepository springDataProductRepository;

    private final ProductMapper productMapper;

    @Override
    public Product save(Product product) {
        ProductEntity entity = productMapper.toEntity(product);

        return productMapper.toDomain(springDataProductRepository.save(entity));
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return springDataProductRepository.findById(id).map(productMapper::toDomain); 
    }

    @Override
    public List<Product> findByUserId(UUID userId) {
        return springDataProductRepository.findByUserId(userId).stream()
        .map(productMapper::toDomain)
        .collect(Collectors.toList());
    }

    @Override
    public List<Product> findAll() {
        return springDataProductRepository.findAll().stream()
        .map(productMapper::toDomain)
        .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        springDataProductRepository.deleteById(id);
    }
    
}
