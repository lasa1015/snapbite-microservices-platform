//package com.shaluo.orderservice.controller;
//
//import com.shaluo.orderservice.service.OrderMerchantService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/order/merchant")
//public class OrderMerchantController {
//
//    @Autowired
//    private OrderMerchantService orderMerchantService;
//
//    // 获取登录商户的所有订单
//    @GetMapping("/my-restaurant-orders")
//    public ResponseEntity<?> getMyRestaurantOrders(Authentication auth) {
//
//        // 获取商家账号name
//        String merchantUsername = auth.getName();
//
//        return ResponseEntity.ok(orderMerchantService.getMyRestaurantOrders(merchantUsername));
//    }
//
//
//    // ✅ 商户发货
//    @PatchMapping("/ship/{orderId}")
//    public ResponseEntity<?> markAsShipped(@PathVariable UUID orderId, Authentication auth) {
//        String merchantUsername = auth.getName();
//        String restaurantId = getRestaurantIdByMerchantUsername(merchantUsername); // 你要实现这个逻辑
//        orderMerchantService.markAsShipped(orderId, restaurantId);
//        return ResponseEntity.ok("已发货");
//    }
//
//    // ✅ 商户取消订单
//    @PatchMapping("/cancel/{orderId}")
//    public ResponseEntity<?> cancelByMerchant(@PathVariable UUID orderId, Authentication auth) {
//        String merchantUsername = auth.getName();
//        String restaurantId = getRestaurantIdByMerchantUsername(merchantUsername); // 你要实现这个逻辑
//        orderMerchantService.cancelOrder(orderId, restaurantId);
//        return ResponseEntity.ok("已取消订单");
//    }
//
//    private String getRestaurantIdByMerchantUsername(String username) {
//        return restaurantRepository.findByOwnerUsername(username)
//                .orElseThrow(() -> new RuntimeException("未找到该商户对应的餐厅"))
//                .getId()
//                .toString(); // 如果你的 restaurant.id 是 Long，就转成字符串
//    }
//
//}
