package com.shaluo.snapbite.controller;

import com.shaluo.snapbite.dto.AddCartItemRequest;
import com.shaluo.snapbite.dto.CartItemDetailResponse;
import com.shaluo.snapbite.model.mongo.CartItem;
import com.shaluo.snapbite.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // 获取当前登录用户的购物车
    @GetMapping
    public List<CartItemDetailResponse> getCart(Authentication auth) {
        // Authentication auth 是 Spring Security 自动注入的参数，用于表示当前请求的认证信息（如果用户已登录）

        String username = auth.getName();

        // 找出用户的购物车
        return cartService.getCartWithDish(username);
    }

    // 向购物车添加菜品（或已存在则累加数量）
    @PostMapping("/add")
    public CartItem addToCart(Authentication auth,  @RequestBody AddCartItemRequest req) {

        String username = auth.getName();

        return cartService.addOrUpdate(username, req.getDishId(), req.getRestaurantId(), req.getQuantity());
    }

    // 修改某条购物车项的数量
    @PutMapping("/{itemId}/quantity")
    public void updateQuantity(@PathVariable String itemId,
                               @RequestParam int quantity) {

        cartService.updateQuantity(itemId, quantity);
    }

    // 删除某条购物车项
    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable String itemId) {

        cartService.deleteItem(itemId);
    }

    // 清空当前用户的购物车
    @DeleteMapping("/clear")
    public void clearCart(Authentication auth) {

        String username = auth.getName();

        cartService.clearCart(username);
    }
}
