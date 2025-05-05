package com.shaluo.snapbite.controller;

import com.shaluo.snapbite.repository.postgres.RestaurantRepository;
import com.shaluo.snapbite.service.OrderMerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/order/merchant")
public class OrderMerchantController {

    @Autowired
    private OrderMerchantService orderMerchantService;

    @Autowired
    private RestaurantRepository restaurantRepository;


    // ✅ 获取指定商户的所有订单（根据餐厅 ID 查询）
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<?> getOrdersByRestaurant(@PathVariable String restaurantId, Authentication auth) {
        // 如你后续要限制商户权限，可以使用 merchantUsername 进一步校验
        String merchantUsername = auth.getName();
        return ResponseEntity.ok(orderMerchantService.getOrdersByRestaurant(restaurantId));
    }

    // ✅ 商户发货
    @PatchMapping("/ship/{orderId}")
    public ResponseEntity<?> markAsShipped(@PathVariable UUID orderId, Authentication auth) {
        String merchantUsername = auth.getName();
        String restaurantId = getRestaurantIdByMerchantUsername(merchantUsername); // 你要实现这个逻辑
        orderMerchantService.markAsShipped(orderId, restaurantId);
        return ResponseEntity.ok("已发货");
    }

    // ✅ 商户取消订单
    @PatchMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelByMerchant(@PathVariable UUID orderId, Authentication auth) {
        String merchantUsername = auth.getName();
        String restaurantId = getRestaurantIdByMerchantUsername(merchantUsername); // 你要实现这个逻辑
        orderMerchantService.cancelOrder(orderId, restaurantId);
        return ResponseEntity.ok("已取消订单");
    }

    private String getRestaurantIdByMerchantUsername(String username) {
        return restaurantRepository.findByOwnerUsername(username)
                .orElseThrow(() -> new RuntimeException("未找到该商户对应的餐厅"))
                .getId()
                .toString(); // 如果你的 restaurant.id 是 Long，就转成字符串
    }

}
