package com.shaluo.orderservice.event;

import java.util.UUID;

public class OrderCreatedEvent {
    private UUID orderId;
    private String username;
    private String restaurantId;
    private Double totalPrice;

    public OrderCreatedEvent() {}

    public OrderCreatedEvent(UUID orderId, String username, String restaurantId, Double totalPrice) {
        this.orderId = orderId;
        this.username = username;
        this.restaurantId = restaurantId;
        this.totalPrice = totalPrice;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
