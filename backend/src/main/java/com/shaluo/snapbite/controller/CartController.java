package com.shaluo.snapbite.controller;

import com.shaluo.snapbite.dto.AddCartItemRequest;
import com.shaluo.snapbite.dto.CartItemWithDish;
import com.shaluo.snapbite.model.mongo.CartItem;
import com.shaluo.snapbite.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车接口（方案 2）
 *
 * 1.  GET  /api/cart                 返回带菜品信息的购物车（CartItemWithDish）
 * 2.  POST /api/cart/add             添加／累加菜品
 * 3.  PUT  /api/cart/{id}/quantity   修改数量
 * 4.  DELETE /api/cart/{id}          删除单行
 * 5.  DELETE /api/cart/clear         清空购物车
 *
 * 说明：
 * • “登录用户”用 username 做分区；“游客”用 sessionId 做分区。
 * • 后端 addOrUpdate 已用 (restaurantId + dishId) 当唯一键，
 *   不同餐厅的同一道菜会分开存。
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /* ========= 1. 查询购物车（直接带菜品信息） ========= */
    @GetMapping
    public List<CartItemWithDish> getCart(Authentication auth,
                                          HttpServletRequest request) {

        String username  = auth != null ? auth.getName() : null;
        String sessionId = request.getSession(true).getId();

        return cartService.getCartWithDish(username, sessionId);
    }

    /* ========= 2. 添加 / 累加 ========= */
    @PostMapping("/add")
    public CartItem addToCart(Authentication auth,
                              @RequestBody AddCartItemRequest req,
                              HttpServletRequest request) {

        String username  = auth != null ? auth.getName() : null;
        String sessionId = request.getSession(true).getId();

        return cartService.addOrUpdate(
                username,
                sessionId,
                req.getDishId(),
                req.getRestaurantId(),
                req.getQuantity()
        );
    }

    /* ========= 3. 修改数量 ========= */
    @PutMapping("/{itemId}/quantity")
    public void updateQuantity(@PathVariable String itemId,
                               @RequestParam int quantity) {

        cartService.updateQuantity(itemId, quantity);
    }

    /* ========= 4. 删除一行 ========= */
    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable String itemId) {
        cartService.deleteItem(itemId);
    }

    /* ========= 5. 清空购物车 ========= */
    @DeleteMapping("/clear")
    public void clearCart(Authentication auth,
                          HttpServletRequest request) {

        String username  = auth != null ? auth.getName() : null;
        String sessionId = request.getSession(true).getId();
        cartService.clearCart(username, sessionId);
    }
}
