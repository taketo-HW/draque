package com.example.dockerapi.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 注文のドメインモデル
 */
public class Order {
    private Long id;
    private User user;
    private List<OrderItem> items;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;

    public Order() {
        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.orderDate = LocalDateTime.now();
        this.totalAmount = BigDecimal.ZERO;
    }

    public Order(Long id, User user) {
        this();
        this.id = id;
        this.user = user;
    }

    public Order(User user) {
        this();
        this.user = user;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    public void setItems(List<OrderItem> items) {
        this.items = new ArrayList<>(items);
        calculateTotal();
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    // ドメインロジック
    public void addItem(Product product, int quantity) {
        if (product.canPurchase(quantity)) {
            OrderItem item = new OrderItem(product, quantity);
            items.add(item);
            product.reduceStock(quantity);
            calculateTotal();
        } else {
            throw new IllegalStateException("商品の在庫が不足しています");
        }
    }

    public void removeItem(OrderItem item) {
        if (items.remove(item)) {
            item.getProduct().addStock(item.getQuantity());
            calculateTotal();
        }
    }

    public void confirm() {
        if (status == OrderStatus.PENDING && !items.isEmpty()) {
            status = OrderStatus.CONFIRMED;
        } else {
            throw new IllegalStateException("注文を確定できません");
        }
    }

    public void cancel() {
        if (status == OrderStatus.PENDING || status == OrderStatus.CONFIRMED) {
            // 在庫を戻す
            for (OrderItem item : items) {
                item.getProduct().addStock(item.getQuantity());
            }
            status = OrderStatus.CANCELLED;
        }
    }

    private void calculateTotal() {
        this.totalAmount = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user=" + user +
                ", items=" + items +
                ", status=" + status +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                '}';
    }

    /**
     * 注文ステータス
     */
    public enum OrderStatus {
        PENDING, // 保留中
        CONFIRMED, // 確定済み
        SHIPPED, // 発送済み
        DELIVERED, // 配達済み
        CANCELLED // キャンセル
    }

    /**
     * 注文アイテム
     */
    public static class OrderItem {
        private Product product;
        private int quantity;
        private BigDecimal unitPrice;

        public OrderItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
            this.unitPrice = product.getPrice();
        }

        public Product getProduct() {
            return product;
        }

        public int getQuantity() {
            return quantity;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public BigDecimal getSubtotal() {
            return unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
