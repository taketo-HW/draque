package com.example.dockerapi.domain.repository;

import com.example.dockerapi.domain.entity.Order;
import java.util.List;
import java.util.Optional;

/**
 * 注文リポジトリインターフェース
 */
public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(Long id);
    List<Order> findAll();
    void deleteById(Long id);
}
