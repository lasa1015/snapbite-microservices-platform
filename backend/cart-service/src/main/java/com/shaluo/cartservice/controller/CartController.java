package com.shaluo.cartservice.controller;

import com.shaluo.cartservice.dto.response.RestaurantCartGroup;
import com.shaluo.cartservice.model.mongo.CartItem;
import com.shaluo.cartservice.service.CartService;
import com.shaluo.cartservice.dto.request.AddCartItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // 【获取当前登录用户的购物车内容】
    @GetMapping
    public List<RestaurantCartGroup> getCart(Authentication auth) {
        // Authentication auth 是 Spring Security 自动注入的参数，用于表示当前请求的认证信息（如果用户已登录）

        // 获取用户名
        String username = auth.getName();

        return cartService.getGroupedCart(username);
    }

    // 【向购物车添加菜品（或已存在则累加数量）】
    @PostMapping("/add")
    public CartItem addToCart(Authentication auth, @RequestBody AddCartItemRequest req) {

        String username = auth.getName();

        return cartService.addOrUpdate(username, req);
    }


    // 【修改某条购物车项的数量】
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

    // Feign 无法帮你自动注入 Authentication
    // 【需要在 cart-service 中单独暴露一个新的接口，专门给order-service调用】
    // 注意：这个接口不依赖 Spring Security，而是直接传入 username
    @GetMapping("/user/{username}")
    public List<RestaurantCartGroup> getCartByUsername(@PathVariable String username) {
        return cartService.getGroupedCart(username);
    }

    // 【给order-service用的清空购物车api】
    @DeleteMapping("/internal/clear/{username}")
    public void clearCartByUsername(@PathVariable String username) {
        cartService.clearCart(username);
    }


}
