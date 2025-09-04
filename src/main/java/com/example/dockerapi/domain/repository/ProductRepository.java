package com.example.dockerapi.domain.repository;

import com.example.dockerapi.domain.entity.Product;
import java.util.List;
import java.util.Optional;

/**
 * 商品リポジトリインターフェース
 */
public interface ProductRepository {
    Product save(Product product);

    Optional<Product> findById(Long id);

    List<Product> findAll();

    void deleteById(Long id);
}
