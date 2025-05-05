// dto/OrderResponse.java
package com.shaluo.snapbite.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private String id;
    private String address;
    private String recipient;
    private String phone;
    private String status;
    private Double totalPrice;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
}
