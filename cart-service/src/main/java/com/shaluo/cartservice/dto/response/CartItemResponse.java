package com.shaluo.cartservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemResponse {
    private String id;
    private String dishId;
    private String dishName;
    private Double price;
    private int quantity;

}
