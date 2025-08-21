package com.example.dockerapi.infrastructure.persistence;

import com.example.dockerapi.domain.model.Product;
import com.example.dockerapi.domain.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 商品リポジトリの実装クラス
 */
@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    @Autowired
    public ProductRepositoryImpl(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = convertToEntity(product);
        ProductEntity savedEntity = productJpaRepository.save(entity);
        return convertToDomain(savedEntity);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id).map(this::convertToDomain);
    }

    @Override
    public List<Product> findByName(String name) {
        return productJpaRepository.findByName(name).stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByCategory(String category) {
        return productJpaRepository.findByCategory(category).stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findInStock() {
        return productJpaRepository.findByStockQuantityGreaterThan(0).stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findAll() {
        return productJpaRepository.findAll().stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        productJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return productJpaRepository.existsById(id);
    }

    /**
     * ドメインモデルをエンティティに変換する
     */
    private ProductEntity convertToEntity(Product product) {
        ProductEntity entity = new ProductEntity();
        entity.setId(product.getId());
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setPrice(product.getPrice());
        entity.setStockQuantity(product.getStockQuantity());
        entity.setCategory(product.getCategory());
        return entity;
    }

    /**
     * エンティティをドメインモデルに変換する
     */
    private Product convertToDomain(ProductEntity entity) {
        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getStockQuantity(),
                entity.getCategory());
    }
}
