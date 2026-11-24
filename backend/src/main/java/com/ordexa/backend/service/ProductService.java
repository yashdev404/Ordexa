package com.ordexa.backend.service;

import com.ordexa.backend.dto.ProductDto;
import com.ordexa.backend.entity.ProductEntity;
import com.ordexa.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductEntity> getProductsByCompany(UUID companyId) {
        return productRepository.findByCompanyId(companyId);
    }

    public ProductEntity getProductById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
    }

    public ProductEntity getProductBySku(UUID companyId, String sku) {
        return productRepository.findByCompanyIdAndSku(companyId, sku)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with SKU: " + sku));
    }

    @Transactional
    public ProductEntity createProduct(ProductDto.CreateProductRequest request) {
        if (productRepository.existsByCompanyIdAndSku(request.getCompanyId(), request.getSku())) {
            throw new IllegalArgumentException("A product with SKU '" + request.getSku() + "' already exists for this company.");
        }

        ProductEntity product = new ProductEntity(
                request.getCompanyId(),
                request.getSku(),
                request.getName(),
                request.getUom(),
                request.getUnitPrice()
        );

        return productRepository.save(product);
    }

    @Transactional
    public ProductEntity updateProduct(UUID id, ProductDto.UpdateProductRequest request) {
        ProductEntity product = getProductById(id);

        if (request.getName() != null && !request.getName().isBlank()) {
            product.setName(request.getName());
        }
        if (request.getUom() != null && !request.getUom().isBlank()) {
            product.setUom(request.getUom());
        }
        if (request.getUnitPrice() != null) {
            product.setUnitPrice(request.getUnitPrice());
        }

        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}
