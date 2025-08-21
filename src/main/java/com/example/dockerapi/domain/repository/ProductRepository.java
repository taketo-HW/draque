package com.example.dockerapi.domain.repository;

import com.example.dockerapi.domain.model.Product;
import java.util.List;
import java.util.Optional;

/**
 * 商品リポジトリのインターフェース
 */
public interface ProductRepository {

    /**
     * 商品を保存する
     */
    Product save(Product product);

    /**
     * IDで商品を検索する
     */
    Optional<Product> findById(Long id);

    /**
     * 名前で商品を検索する
     */
    List<Product> findByName(String name);

    /**
     * カテゴリで商品を検索する
     */
    List<Product> findByCategory(String category);

    /**
     * 在庫がある商品を取得する
     */
    List<Product> findInStock();

    /**
     * 全商品を取得する
     */
    List<Product> findAll();

    /**
     * 商品を削除する
     */
    void deleteById(Long id);

    /**
     * 商品が存在するかチェックする
     */
    boolean existsById(Long id);
}
