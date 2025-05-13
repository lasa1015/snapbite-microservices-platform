package com.shaluo.orderservice.dto.external;

import lombok.Data;

@Data
public class CartItemResponse {
    private String id;
    private String dishId;
    private String dishName;
    private Double dishPrice;
    private int quantity;
}