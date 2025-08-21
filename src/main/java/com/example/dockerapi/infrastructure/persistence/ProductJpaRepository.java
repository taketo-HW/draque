package com.example.dockerapi.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品のJPAリポジトリインターフェース
 */
@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {

    /**
     * 名前で商品を検索する
     */
    List<ProductEntity> findByName(String name);

    /**
     * カテゴリで商品を検索する
     */
    List<ProductEntity> findByCategory(String category);

    /**
     * 在庫がある商品を取得する
     */
    List<ProductEntity> findByStockQuantityGreaterThan(int quantity);
}
