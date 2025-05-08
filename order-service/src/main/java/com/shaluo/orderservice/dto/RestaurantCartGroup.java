package com.shaluo.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RestaurantCartGroup {
    private String restaurantId;

    private String restaurantName;
    private List<CartItemResponse> items;
}