// dto/RestaurantCartGroup.java
package com.shaluo.cartservice.dto.response;

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

/*
返回值范例




 */