package com.shaluo.orderservice.client;

import com.shaluo.orderservice.dto.RestaurantCartGroup;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

// Feign 客户端：声明式地调用 cart-service
@FeignClient(name = "cart-service", url = "http://localhost:8083")
public interface CartClient {

    // 获取某个用户的分组购物车内容（按餐厅）
    @GetMapping("/api/cart/user/{username}")
    List<RestaurantCartGroup> getGroupedCart(@PathVariable("username") String username);

    // 清空购物车
    @DeleteMapping("/api/cart/internal/clear/{username}")
    void clearCart(@PathVariable("username") String username);

}
