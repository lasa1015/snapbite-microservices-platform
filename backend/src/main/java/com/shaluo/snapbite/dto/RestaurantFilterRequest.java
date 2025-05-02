package com.shaluo.snapbite.dto;

import lombok.Data;

import java.util.List;

/**
 * 用于前端发送的筛选条件请求体
 */
@Data
public class RestaurantFilterRequest {
    private List<String> categories; // "Pizza", "Chinese"...
    private List<String> prices;     // "€", "€€"...
    private List<String> meals;      // "breakfast", "lunch", "dinner", "brunch"
}
