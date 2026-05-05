package com.hexagonal.practica.domain.model.product;

import java.math.BigDecimal;
import java.util.UUID;
import com.hexagonal.practica.domain.exception.DomainException;

public class Product {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imagePath;
    private UUID userId;

    private Product() {}

    public static Product create(String name, String description, BigDecimal price, String imagePath, UUID userId) {
        if (name == null || name.trim().isEmpty()) {
            throw new DomainException("El nombre del producto es obligatorio.");
        }
        if (userId == null) {
            throw new DomainException("El producto debe pertenecer a un usuario registrado.");
        }
        
        Product product = new Product();
        product.id = UUID.randomUUID();
        product.name = name;
        product.description = description;
        product.price = price;
        product.imagePath = imagePath;
        product.userId = userId;
        return product;
    }

    
    public static Product reconstruct(UUID id, String name, String description, BigDecimal price, String imagePath, UUID userId) {
        Product product = new Product();
        product.id = id;
        product.name = name;
        product.description = description;
        product.price = price;
        product.imagePath = imagePath;
        product.userId = userId;
        return product;
    }

  
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public String getImagePath() { return imagePath; }
    public UUID getUserId() { return userId; }

    
    public void updateInfo(String name, String description, BigDecimal price, String newImagePath) {
        if (name == null || name.trim().isEmpty()) {
            throw new DomainException("El nombre del producto no puede estar vacío.");
        }
        this.name = name;
        this.description = description;
        this.price = price;
        
        
        if (newImagePath != null && !newImagePath.trim().isEmpty()) {
            this.imagePath = newImagePath;
        }
    }

}

