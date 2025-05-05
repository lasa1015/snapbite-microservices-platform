package com.shaluo.snapbite.controller;

import com.shaluo.snapbite.dto.CheckoutRequest;
import com.shaluo.snapbite.model.postgres.User;
import com.shaluo.snapbite.repository.postgres.UserRepository;
import com.shaluo.snapbite.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 用户结算 → 生成订单
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody CheckoutRequest req, Authentication auth) {
        String username = auth.getName(); // 只传 username，具体逻辑交给 service

        orderService.checkout(username, req);

        return ResponseEntity.ok("✅ 下单成功！");
    }


    // 获取用户所有订单
    @GetMapping("/my-orders")
    public ResponseEntity<?> getMyOrders(Authentication auth) {
        String username = auth.getName();
        return ResponseEntity.ok(orderService.getUserOrders(username));
    }


}
