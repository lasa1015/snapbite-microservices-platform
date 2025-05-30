package com.shaluo.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "restaurant-service", contextId = "restaurantClient", url = "${RESTAURANT_SERVICE_URL}")
public interface RestaurantClient {

    // 根据商户用户名查找其名下的餐厅 ID
    @GetMapping("/api/restaurants/merchant/{username}")
    String getRestaurantIdByOwnerUsername(@PathVariable("username") String username);
}
