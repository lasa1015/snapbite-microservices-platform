// dto/OrderItemResponse.java
package com.shaluo.snapbite.dto;

import lombok.Data;

@Data
public class OrderItemResponse {
    private String dishName;
    private Double price;
    private Integer quantity;
    private String restaurantId;
}
