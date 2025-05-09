package com.shaluo.orderservice.controller;

import com.shaluo.orderservice.client.RestaurantClient;
import com.shaluo.orderservice.dto.response.OrderResponse;
import com.shaluo.orderservice.service.OrderMerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order/merchant")
public class OrderMerchantController {

    @Autowired
    private OrderMerchantService orderMerchantService;

    @Autowired
    private RestaurantClient restaurantClient;

    // 获取当前登录商户的所有订单
    @GetMapping("/my-restaurant-orders")
    public ResponseEntity<?> getMyRestaurantOrders(Authentication auth) {

        // 从 Authentication 对象中拿到当前登录商户的用户名
        String merchantUsername = auth.getName();

        // 调用 service 查询商户订单列表
        List<OrderResponse> orders = orderMerchantService.getMyRestaurantOrders(merchantUsername);

        // 返回 200 + JSON 数据
        return ResponseEntity.ok(orders);
    }

    // 商户接单（将订单状态从 CREATED → ACCEPTED）
    @PatchMapping("/accept/{orderId}")
    public ResponseEntity<?> acceptOrder(@PathVariable UUID orderId, Authentication auth) {
        String merchantUsername = auth.getName();
        String restaurantId = restaurantClient.getRestaurantIdByOwnerUsername(merchantUsername);
        orderMerchantService.acceptOrder(orderId, restaurantId);
        return ResponseEntity.ok("订单已接单");
    }

    // 商户发货
    @PatchMapping("/ship/{orderId}")
    public ResponseEntity<?> markAsShipped(@PathVariable UUID orderId, Authentication auth) {

        // 从 Authentication 对象中拿到当前登录商户的用户名
        String merchantUsername = auth.getName();

        // 调用 restaurant-service 查询该商户名下的餐厅 ID（通过 Feign）
        // Service 不依赖 FeignClient，从而更容易测试、复用。
        String restaurantId = restaurantClient.getRestaurantIdByOwnerUsername(merchantUsername);

        // 将 orderId 和 restaurantId 交给 Service 验证并处理发货逻辑
        orderMerchantService.markAsShipped(orderId, restaurantId);

        return ResponseEntity.ok("已发货");
    }


    // 商户取消订单
    @PatchMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelByMerchant(@PathVariable UUID orderId, Authentication auth) {

        String merchantUsername = auth.getName();

        String restaurantId = restaurantClient.getRestaurantIdByOwnerUsername(merchantUsername);

        orderMerchantService.cancelOrder(orderId, restaurantId);

        return ResponseEntity.ok("已取消订单");
    }
}
