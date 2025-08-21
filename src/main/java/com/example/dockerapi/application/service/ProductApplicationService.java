package com.example.dockerapi.application.service;

import com.example.dockerapi.application.dto.ProductDto;
import com.example.dockerapi.domain.model.Product;
import com.example.dockerapi.domain.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 商品のアプリケーションサービス
 */
@Service
@Transactional
public class ProductApplicationService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductApplicationService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * 商品を作成する
     */
    public ProductDto createProduct(String name, String description, BigDecimal price, int stockQuantity,
            String category) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStockQuantity(stockQuantity);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return convertToDto(savedProduct);
    }

    /**
     * 商品を更新する
     */
    public ProductDto updateProduct(Long id, String name, String description, BigDecimal price, int stockQuantity,
            String category) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setStockQuantity(stockQuantity);
            product.setCategory(category);
            Product savedProduct = productRepository.save(product);
            return convertToDto(savedProduct);
        }
        throw new IllegalArgumentException("商品が見つかりません: " + id);
    }

    /**
     * 在庫を追加する
     */
    public ProductDto addStock(Long productId, int quantity) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.addStock(quantity);
            Product savedProduct = productRepository.save(product);
            return convertToDto(savedProduct);
        }
        throw new IllegalArgumentException("商品が見つかりません: " + productId);
    }

    /**
     * 商品を取得する
     */
    @Transactional(readOnly = true)
    public Optional<ProductDto> getProductById(Long id) {
        return productRepository.findById(id).map(this::convertToDto);
    }

    /**
     * 名前で商品を検索する
     */
    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByName(String name) {
        return productRepository.findByName(name).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * カテゴリで商品を検索する
     */
    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByCategory(String category) {
        return productRepository.findByCategory(category).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 在庫がある商品を取得する
     */
    @Transactional(readOnly = true)
    public List<ProductDto> getInStockProducts() {
        return productRepository.findInStock().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 全商品を取得する
     */
    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 商品を削除する
     */
    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("商品が見つかりません: " + id);
        }
    }

    /**
     * ドメインモデルをDTOに変換する
     */
    private ProductDto convertToDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategory());
    }
}
