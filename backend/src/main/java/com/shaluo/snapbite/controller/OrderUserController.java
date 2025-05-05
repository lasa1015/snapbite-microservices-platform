package com.shaluo.snapbite.controller;

import com.shaluo.snapbite.dto.CheckoutRequest;
import com.shaluo.snapbite.service.OrderUserService;
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

    // 用户结算 → 生成订单
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody CheckoutRequest req, Authentication auth) {
        String username = auth.getName(); // 只传 username，具体逻辑交给 service

        orderUserService.checkout(username, req);

        return ResponseEntity.ok("✅ 下单成功！");
    }


    // 获取用户所有订单
    @GetMapping("/my-orders")
    public ResponseEntity<?> getMyOrders(Authentication auth) {
        String username = auth.getName();
        return ResponseEntity.ok(orderUserService.getUserOrders(username));
    }

    @PatchMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable UUID orderId, Authentication auth) {
        String username = auth.getName();
        orderUserService.cancelOrder(username, orderId);
        return ResponseEntity.ok("订单已取消");
    }


    @PatchMapping("/confirm/{orderId}")
    public ResponseEntity<?> confirmOrder(@PathVariable UUID orderId, Authentication auth) {
        String username = auth.getName();
        orderUserService.confirmOrder(username, orderId);
        return ResponseEntity.ok("订单已确认收货");
    }



}
