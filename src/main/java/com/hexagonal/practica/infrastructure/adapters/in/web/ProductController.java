package com.hexagonal.practica.infrastructure.adapters.in.web;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hexagonal.practica.domain.model.product.Product;
import com.hexagonal.practica.domain.ports.in.ManageProductUseCase;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ManageProductUseCase manageProductUseCase;


    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Product> products = manageProductUseCase.findAll(page, size);

        Page<ProductResponse> responsePage = products.map(p -> new ProductResponse(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getImagePath(),
                p.getUserId()));

        return ResponseEntity.ok(responsePage);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id) {
        Product product = manageProductUseCase.findbyID(id);

        ProductResponse response = new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImagePath(),
                product.getUserId());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable UUID id,
            @AuthenticationPrincipal UUID userId,
            @RequestBody ProductRequest request) {
        Product productUpdateInfo = Product.reconstruct(
                null,
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getImagePath(),
                null);

        Product updateProdut = manageProductUseCase.update(id, productUpdateInfo, userId);

        ProductResponse response = new ProductResponse(
                updateProdut.getId(),
                updateProdut.getName(),
                updateProdut.getDescription(),
                updateProdut.getPrice(),
                updateProdut.getImagePath(),
                updateProdut.getUserId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable UUID id,
            @AuthenticationPrincipal UUID userId) {
        manageProductUseCase.deleteById(id, userId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-products")
    public ResponseEntity<List<ProductResponse>> getMyProducts(@AuthenticationPrincipal UUID userId) {
        List<Product> myProducts = manageProductUseCase.findByUserId(userId);

        List<ProductResponse> responseList = myProducts.stream()
                .map(p -> new ProductResponse(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getPrice(),
                        p.getImagePath(),
                        p.getUserId()))
                .toList();

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ProductResponse>> getPublicUserProducts(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Product> productPage = manageProductUseCase.findPaginatedByUserId(userId, page, size);

        Page<ProductResponse> responsePage = productPage.map(p -> new ProductResponse(
            p.getId(), 
            p.getName(), 
            p.getDescription(), 
            p.getPrice(), 
            p.getImagePath(), 
            userId));
            return ResponseEntity.ok(responsePage);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponse> createProduct(
            @AuthenticationPrincipal UUID userId,
            @Valid @ModelAttribute ProductFormDataRequest request) {
        Product productInput = Product.reconstruct(
                null,
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                null,
                userId);

        Product createProduct = manageProductUseCase.create(productInput, request.getImage());

        ProductResponse response = new ProductResponse(
                createProduct.getId(),
                createProduct.getName(),
                createProduct.getDescription(),
                createProduct.getPrice(),
                createProduct.getImagePath(),
                createProduct.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public static class ProductFormDataRequest {
        @NotBlank
        private String name;
        private String description;
        @NotNull
        @Positive
        private BigDecimal price;
        private MultipartFile image;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public MultipartFile getImage() {
            return image;
        }

        public void setImage(MultipartFile image) {
            this.image = image;
        }
    }

    public static class ProductRequest {
        @NotBlank
        private String name;

        private String description;
        @NotNull
        @Positive
        private BigDecimal price;

        private String imagePath;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

    }

    public static class ProductResponse {
        private UUID id;
        private String name;
        private String description;
        private BigDecimal price;
        private String imagePath;
        private UUID userId;

        public ProductResponse(UUID id, String name, String description, BigDecimal price, String imagePath,
                UUID userId) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
            this.imagePath = imagePath;
            this.userId = userId;
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        public UUID getUserId() {
            return userId;
        }

        public void setUserId(UUID userId) {
            this.userId = userId;
        }

    }

}
