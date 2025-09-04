package com.example.dockerapi.application.usecase;

import com.example.dockerapi.application.dto.OrderDto;
import com.example.dockerapi.domain.entity.Order;
import com.example.dockerapi.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 注文関連のユースケース
 */
@Service
public class OrderUseCase {
    private final OrderRepository orderRepository;

    public OrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderDto createOrder(Long userId, Long productId, Integer quantity, BigDecimal totalAmount) {
        Order order = new Order(null, userId, productId, quantity, totalAmount);
        Order savedOrder = orderRepository.save(order);
        return convertToDto(savedOrder);
    }

    public Optional<OrderDto> getOrderById(Long id) {
        return orderRepository.findById(id).map(this::convertToDto);
    }

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    private OrderDto convertToDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getUserId(),
                order.getProductId(),
                order.getQuantity(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
