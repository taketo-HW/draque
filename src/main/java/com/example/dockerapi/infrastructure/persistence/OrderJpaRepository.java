package com.example.dockerapi.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 注文のJPAリポジトリインターフェース
 */
@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {

    /**
     * ユーザーIDで注文を検索する
     */
    List<OrderEntity> findByUserId(Long userId);

    /**
     * ステータスで注文を検索する
     */
    List<OrderEntity> findByStatus(OrderEntity.OrderStatus status);

    /**
     * 期間で注文を検索する
     */
    List<OrderEntity> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);
}
