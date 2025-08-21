package com.example.dockerapi.application.service;

import com.example.dockerapi.application.dto.OrderDto;
import com.example.dockerapi.domain.model.Order;
import com.example.dockerapi.domain.model.Product;
import com.example.dockerapi.domain.model.User;
import com.example.dockerapi.domain.repository.OrderRepository;
import com.example.dockerapi.domain.repository.ProductRepository;
import com.example.dockerapi.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 注文のアプリケーションサービス
 */
@Service
@Transactional
public class OrderApplicationService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderApplicationService(OrderRepository orderRepository, UserRepository userRepository,
            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    /**
     * 注文を作成する
     */
    public OrderDto createOrder(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            Order order = new Order(userOpt.get());
            Order savedOrder = orderRepository.save(order);
            return convertToDto(savedOrder);
        }
        throw new IllegalArgumentException("ユーザーが見つかりません: " + userId);
    }

    /**
     * 注文に商品を追加する
     */
    public OrderDto addItemToOrder(Long orderId, Long productId, int quantity) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        Optional<Product> productOpt = productRepository.findById(productId);

        if (orderOpt.isPresent() && productOpt.isPresent()) {
            Order order = orderOpt.get();
            Product product = productOpt.get();

            order.addItem(product, quantity);
            Order savedOrder = orderRepository.save(order);
            return convertToDto(savedOrder);
        }
        throw new IllegalArgumentException("注文または商品が見つかりません");
    }

    /**
     * 注文を確定する
     */
    public OrderDto confirmOrder(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.confirm();
            Order savedOrder = orderRepository.save(order);
            return convertToDto(savedOrder);
        }
        throw new IllegalArgumentException("注文が見つかりません: " + orderId);
    }

    /**
     * 注文をキャンセルする
     */
    public OrderDto cancelOrder(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.cancel();
            Order savedOrder = orderRepository.save(order);
            return convertToDto(savedOrder);
        }
        throw new IllegalArgumentException("注文が見つかりません: " + orderId);
    }

    /**
     * 注文を取得する
     */
    @Transactional(readOnly = true)
    public Optional<OrderDto> getOrderById(Long id) {
        return orderRepository.findById(id).map(this::convertToDto);
    }

    /**
     * ユーザーの注文を取得する
     */
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 全注文を取得する
     */
    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 注文を削除する
     */
    public void deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("注文が見つかりません: " + id);
        }
    }

    /**
     * ドメインモデルをDTOに変換する
     */
    private OrderDto convertToDto(Order order) {
        List<OrderDto.OrderItemDto> itemDtos = order.getItems().stream()
                .map(item -> new OrderDto.OrderItemDto(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getSubtotal()))
                .collect(Collectors.toList());

        return new OrderDto(
                order.getId(),
                order.getUser().getId(),
                order.getUser().getName(),
                itemDtos,
                order.getStatus().name(),
                order.getOrderDate(),
                order.getTotalAmount());
    }
}
