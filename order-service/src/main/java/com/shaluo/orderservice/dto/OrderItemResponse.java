package com.shaluo.orderservice.dto;

import lombok.Data;

@Data
public class OrderItemResponse {
    private String dishId;
    private String dishName;
    private Double price;
    private Integer quantity;
    private String restaurantId;
    private Double subtotal; // ✅ 新增：小计
}
