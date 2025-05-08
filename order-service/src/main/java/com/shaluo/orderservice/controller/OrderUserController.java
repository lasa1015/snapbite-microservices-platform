package com.shaluo.orderservice.controller;

import com.shaluo.orderservice.dto.request.CheckoutRequest;
import com.shaluo.orderservice.service.OrderUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/order")
public class OrderUserController {

    @Autowired
    private OrderUserService orderUserService;

    // 【用户结算 → 生成订单】
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody CheckoutRequest request, Authentication authentication) {

        String username = authentication.getName(); // 从 token 中解析用户名

        orderUserService.checkout(username, request);

        return ResponseEntity.ok().build();
    }



    //  获取用户所有订单
    @GetMapping("/my-orders")
    public ResponseEntity<?> getMyOrders(Authentication auth) {
        String username = auth.getName();
        return ResponseEntity.ok(orderUserService.getUserOrders(username));
    }

    // 取消订单
    @PatchMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable UUID orderId, Authentication auth) {
        String username = auth.getName();
        orderUserService.cancelOrder(username, orderId);
        return ResponseEntity.ok("订单已取消");
    }

    // 确认收货
    @PatchMapping("/confirm/{orderId}")
    public ResponseEntity<?> confirmOrder(@PathVariable UUID orderId, Authentication auth) {
        String username = auth.getName();
        orderUserService.confirmOrder(username, orderId);
        return ResponseEntity.ok("订单已确认收货");
    }



}
