package com.shaluo.snapbite.controller;

import com.shaluo.snapbite.dto.OrderRequestDto;
import com.shaluo.snapbite.model.Order;
import com.shaluo.snapbite.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDto req, Authentication auth) {
        Long userId = getCurrentUserId(auth);
        Order order = orderService.createOrder(
                userId,
                req.getRestaurantId().longValue(),  // 👈 把 Integer 转为 Long
                req.getAddress(),
                req.getItems(),
                req.getTotalPrice()
        );
        return ResponseEntity.ok(order);
    }


    /** ⚠️ 注意：假设 auth.getName() 存的是 userId 字符串 */
    private Long getCurrentUserId(Authentication auth) {
        if (auth == null || auth.getName() == null) {
            throw new RuntimeException("用户未登录");
        }
        try {
            return Long.parseLong(auth.getName());
        } catch (NumberFormatException e) {
            throw new RuntimeException("用户 ID 无效：" + auth.getName());
        }
    }
}
