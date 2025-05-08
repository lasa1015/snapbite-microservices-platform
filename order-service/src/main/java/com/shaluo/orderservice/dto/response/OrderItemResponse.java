package com.shaluo.orderservice.dto.response;

import lombok.Data;

@Data
public class OrderItemResponse {
    private String dishId;
    private String dishName;
    private Double price;
    private Integer quantity;
    private String restaurantId;
    private Double subtotal;
}
