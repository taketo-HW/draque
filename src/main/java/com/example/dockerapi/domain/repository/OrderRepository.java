package com.example.dockerapi.domain.repository;

import com.example.dockerapi.domain.model.Order;
import com.example.dockerapi.domain.model.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 注文リポジトリのインターフェース
 */
public interface OrderRepository {

    /**
     * 注文を保存する
     */
    Order save(Order order);

    /**
     * IDで注文を検索する
     */
    Optional<Order> findById(Long id);

    /**
     * ユーザーの注文を取得する
     */
    List<Order> findByUser(User user);

    /**
     * ユーザーIDで注文を取得する
     */
    List<Order> findByUserId(Long userId);

    /**
     * ステータスで注文を取得する
     */
    List<Order> findByStatus(Order.OrderStatus status);

    /**
     * 期間で注文を取得する
     */
    List<Order> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);

    /**
     * 全注文を取得する
     */
    List<Order> findAll();

    /**
     * 注文を削除する
     */
    void deleteById(Long id);

    /**
     * 注文が存在するかチェックする
     */
    boolean existsById(Long id);
}
