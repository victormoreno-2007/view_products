package com.hexagonal.practica.application.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hexagonal.practica.domain.exception.BusinessErrorCode;
import com.hexagonal.practica.domain.exception.DomainException;
import com.hexagonal.practica.domain.model.product.Product;
import com.hexagonal.practica.domain.ports.in.ManageProductUseCase;
import com.hexagonal.practica.domain.ports.out.ImageStoragePort;
import com.hexagonal.practica.domain.ports.out.ProductRepositoryPort;

@Service
public class ProductService implements ManageProductUseCase{

    private final ProductRepositoryPort productRepositoryPort;
    private final ImageStoragePort imageStoragePort;

    public ProductService(ProductRepositoryPort productRepositoryPort, ImageStoragePort imageStoragePort){ 
        this.productRepositoryPort = productRepositoryPort;
        this.imageStoragePort = imageStoragePort;
    }

    @Override
    public Product create(Product product) {
        return productRepositoryPort.save(product);
    }

    @Override
    public Page<Product> findAll(int page, int size) {
        return productRepositoryPort.findAll(page, size);
    }

    @Override
    public List<Product> findByUserId(UUID userId) {
        return productRepositoryPort.findByUserId(userId); 
    }

    @Override
    public Product findbyID(UUID id) {
        return productRepositoryPort.findById(id).orElseThrow(() -> new DomainException(BusinessErrorCode.PRODUCT_NOT_FOUND));
    }

    @Override
    public Product update(UUID id, Product product, UUID userId) {
        Product existProduct = findbyID(id);

        if (!existProduct.getUserId().equals(userId)) {
            throw new DomainException(BusinessErrorCode.ACCESS_DENIED);
        }
        existProduct.updateInfo(
            product.getName(), 
            product.getDescription(), 
            product.getPrice(),
            product.getImagePath());
        
        return productRepositoryPort.save(existProduct);

    }

    @Override
    public void deleteById(UUID id, UUID userId) {
        Product existProduct = findbyID(id);

        if (!existProduct.getUserId().equals(userId)) {
            throw new DomainException(BusinessErrorCode.ACCESS_DENIED);
        }
        productRepositoryPort.deleteById(id);
    }

    @Override
    public Page<Product> findPaginatedByUserId(UUID userId, int page, int size) {
        // El servicio simplemente delega el trabajo al puerto de salida
        return productRepositoryPort.findPaginatedByUserId(userId, page, size);
    }

    @Override
public Product create(Product product, MultipartFile image) {
    String imageUrl = null;
    if (image != null && !image.isEmpty()) {
        imageUrl = imageStoragePort.uploadImage(image);
    }
    
    product.updateInfo(product.getName(), product.getDescription(), product.getPrice(), imageUrl);
    
    return productRepositoryPort.save(product);
}

    
    
}
