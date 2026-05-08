package com.hexagonal.practica.application.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hexagonal.practica.domain.exception.DomainException;
import com.hexagonal.practica.domain.model.product.Product;
import com.hexagonal.practica.domain.ports.in.ManageProductUseCase;
import com.hexagonal.practica.domain.ports.out.ProductRepositoryPort;

@Service
public class ProductService implements ManageProductUseCase{

    private final ProductRepositoryPort productRepositoryPort;

    public ProductService(ProductRepositoryPort productRepositoryPort){ 
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public Product create(Product product) {
        return productRepositoryPort.save(product);
    }

    @Override
    public List<Product> findAll() {
        return productRepositoryPort.findAll();
    }

    @Override
    public List<Product> findByUserId(UUID userId) {
        return productRepositoryPort.findByUserId(userId); 
    }

    @Override
    public Product findbyID(UUID id) {
        return productRepositoryPort.findById(id).orElseThrow(() -> new DomainException("Producto no encontrado"));
    }

    @Override
    public Product update(UUID id, Product product) {
        Product existProduct = findbyID(id);

        existProduct.updateInfo(
            product.getName(), 
            product.getDescription(), 
            product.getPrice(),
            product.getImagePath());
        
        return productRepositoryPort.save(existProduct);

    }

    @Override
    public void deleteById(UUID id) {
        productRepositoryPort.deleteById(id);
    }
    
}
