package com.shaluo.snapbite.dto;

import lombok.Data;

@Data
public class AddCartItemRequest {
    private String dishId;
    private String restaurantId;
    private int quantity;
}
