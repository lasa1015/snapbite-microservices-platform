package com.shaluo.snapbite.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {
    private Integer restaurantId;
    private String address;
    private List<OrderItemDto> items;
    private Double totalPrice;
}
