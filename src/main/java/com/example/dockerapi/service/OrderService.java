package com.example.dockerapi.service;

import com.example.dockerapi.entity.Order;
import com.example.dockerapi.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Order order) {
        validateOrder(order);
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
        return orderRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrder(Order order) {
        validateOrder(order);
        if (order.getId() == null) {
            throw new IllegalArgumentException("Order ID cannot be null for update");
        }
        if (!orderRepository.existsById(order.getId())) {
            throw new IllegalArgumentException("Order not found with ID: " + order.getId());
        }
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
        if (!orderRepository.existsById(id)) {
            throw new IllegalArgumentException("Order not found with ID: " + id);
        }
        orderRepository.deleteById(id);
    }

    private void validateOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (order.getName() == null || order.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Order name cannot be null or empty");
        }
    }
}
