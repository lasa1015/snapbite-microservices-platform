package com.shaluo.orderservice.dto;

import lombok.Data;

@Data
public class CartItemResponse {
    private String id;
    private String dishId;
    private String dishName;
    private Double price;
    private int quantity;
}