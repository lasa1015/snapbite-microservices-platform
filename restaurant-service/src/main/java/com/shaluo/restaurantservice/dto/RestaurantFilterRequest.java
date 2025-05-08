package com.shaluo.restaurantservice.dto;

import lombok.Data;

import java.util.List;

// 请求 DTO：	前端发送筛选条件时用的
// 这个 DTO 不含餐厅信息，它只是查询条件
@Data
public class RestaurantFilterRequest {
    private List<String> categories; // "Pizza", "Chinese"...
    private List<String> prices;     // "€", "€€"...
    private List<String> meals;      // "breakfast", "lunch", "dinner", "brunch"
    private String sortOrder; // 可为 "asc" 或 "desc"，可选字段
}
