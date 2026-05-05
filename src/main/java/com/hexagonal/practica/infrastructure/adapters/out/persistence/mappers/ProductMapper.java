package com.hexagonal.practica.infrastructure.adapters.out.persistence.mappers;

import org.springframework.stereotype.Component;

import com.hexagonal.practica.domain.model.product.Product;
import com.hexagonal.practica.infrastructure.adapters.out.persistence.entities.ProductEntity;


@Component
public class ProductMapper {

    public Product toDomain(ProductEntity entity){
        if (entity == null) {
            return null;
        }

        return Product.reconstruct(
            entity.getId(), 
            entity.getName(), 
            entity.getDescription(), 
            entity.getPrice(), 
            entity.getImagePath(), 
            entity.getUserId());

    }

    public ProductEntity toEntity(Product domain){
        if (domain == null) {
            return null;
        }

        return ProductEntity.builder()
            .id(domain.getId())
            .name(domain.getName())
            .description(domain.getDescription())
            .price(domain.getPrice())
            .imagePath(domain.getImagePath())
            .userId(domain.getUserId())
            .build();
    }
    
}
