package com.example.dockerapi.service;

import com.example.dockerapi.entity.Order;
import com.example.dockerapi.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository repo;

    public OrderService(OrderRepository repo) {
        this.repo = repo;
    }

    public Order createOrder(Order order) {
        return repo.save(order);
    }

    public Optional<Order> getOrderById(Long id) {
        return repo.findById(id);
    }

    public Order updateOrder(Order order) {
        return repo.save(order);
    }

    public void deleteOrder(Long id) {
        repo.deleteById(id);
    }
}
