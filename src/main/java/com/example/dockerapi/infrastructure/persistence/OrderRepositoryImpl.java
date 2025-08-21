package com.example.dockerapi.infrastructure.persistence;

import com.example.dockerapi.domain.model.Order;
import com.example.dockerapi.domain.model.User;
import com.example.dockerapi.domain.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 注文リポジトリの実装クラス
 */
@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Autowired
    public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
    }

    @Override
    public Order save(Order order) {
        OrderEntity entity = convertToEntity(order);
        OrderEntity savedEntity = orderJpaRepository.save(entity);
        return convertToDomain(savedEntity);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderJpaRepository.findById(id).map(this::convertToDomain);
    }

    @Override
    public List<Order> findByUser(User user) {
        // この実装では簡略化のため、ユーザーIDで検索
        return findByUserId(user.getId());
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderJpaRepository.findByUserId(userId).stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByStatus(Order.OrderStatus status) {
        return orderJpaRepository.findByStatus(OrderEntity.OrderStatus.valueOf(status.name())).stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByOrderDateBetween(LocalDateTime start, LocalDateTime end) {
        return orderJpaRepository.findByOrderDateBetween(start, end).stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findAll() {
        return orderJpaRepository.findAll().stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        orderJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return orderJpaRepository.existsById(id);
    }

    /**
     * ドメインモデルをエンティティに変換する
     * 注：この実装は簡略化されており、実際の使用ではより詳細な変換が必要
     */
    private OrderEntity convertToEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        // ユーザーエンティティの変換は簡略化
        if (order.getUser() != null) {
            UserEntity userEntity = new UserEntity();
            userEntity.setId(order.getUser().getId());
            userEntity.setName(order.getUser().getName());
            userEntity.setEmail(order.getUser().getEmail());
            userEntity.setLevel(order.getUser().getLevel());
            userEntity.setExperience(order.getUser().getExperience());
            entity.setUser(userEntity);
        }
        entity.setStatus(OrderEntity.OrderStatus.valueOf(order.getStatus().name()));
        entity.setOrderDate(order.getOrderDate());
        entity.setTotalAmount(order.getTotalAmount());
        return entity;
    }

    /**
     * エンティティをドメインモデルに変換する
     * 注：この実装は簡略化されており、実際の使用ではより詳細な変換が必要
     */
    private Order convertToDomain(OrderEntity entity) {
        if (entity.getUser() == null) {
            throw new IllegalStateException("ユーザー情報が不足しています");
        }

        User user = new User(
                entity.getUser().getId(),
                entity.getUser().getName(),
                entity.getUser().getEmail(),
                entity.getUser().getLevel(),
                entity.getUser().getExperience());

        Order order = new Order(entity.getId(), user);
        order.setStatus(Order.OrderStatus.valueOf(entity.getStatus().name()));
        order.setOrderDate(entity.getOrderDate());
        // 注：アイテムの変換は簡略化

        return order;
    }
}
