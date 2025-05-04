// dto/OrderItemDto.java
package com.shaluo.snapbite.dto;

import lombok.Data;

@Data
public class OrderItemDto {
    private String dishId;
    private String name;
    private Double price;
    private Integer quantity;
}
