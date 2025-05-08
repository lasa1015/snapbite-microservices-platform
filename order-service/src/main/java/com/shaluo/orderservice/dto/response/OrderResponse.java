package com.shaluo.orderservice.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private String id;
    private String restaurantId;
    private String restaurantName; // ✅ 新增：展示用
    private String address;
    private String recipient;
    private String phone;
    private String status;
    private Double totalPrice;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
}
