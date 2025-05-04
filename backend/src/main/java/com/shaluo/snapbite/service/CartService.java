package com.shaluo.snapbite.service;

import com.shaluo.snapbite.dto.CartItemWithDish;

import com.shaluo.snapbite.model.mongo.CartItem;
import com.shaluo.snapbite.model.mongo.Dish;
import com.shaluo.snapbite.model.mongo.Menu;
import com.shaluo.snapbite.repository.mongo.CartItemRepository;
import com.shaluo.snapbite.repository.mongo.MenuRepository;
import com.shaluo.snapbite.repository.RestaurantRepository;
import com.shaluo.snapbite.model.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 购物车业务
 * <p>方案 2：同一个餐厅 + 同一道菜 只存一行。</p>
 */
@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private MenuRepository menuRepository;

    /** 查询餐厅名字用 */
    @Autowired
    private RestaurantRepository restaurantRepository;

    /* ==================================== 基础查询 ==================================== */

    public List<CartItem> getCart(String username, String sessionId) {
        return username != null
                ? cartItemRepository.findByUsername(username)
                : cartItemRepository.findBySessionId(sessionId);
    }

    /* ==================================== 新增 / 累加 ==================================== */

    public CartItem addOrUpdate(String username,
                                String sessionId,
                                String dishId,
                                String restaurantId,
                                int quantity) {

        Optional<CartItem> existing = (username != null)
                ? cartItemRepository.findByUsernameAndRestaurantIdAndDishId(
                username, restaurantId, dishId)
                : cartItemRepository.findBySessionIdAndRestaurantIdAndDishId(
                sessionId, restaurantId, dishId);

        CartItem item = existing.orElse(new CartItem());

        item.setUsername(username);
        item.setSessionId(sessionId);
        item.setDishId(dishId);
        item.setRestaurantId(restaurantId);
        item.setQuantity(item.getId() == null ? quantity : item.getQuantity() + quantity);
        item.setAddedAt(LocalDateTime.now());

        return cartItemRepository.save(item);
    }

    /* ================================ 其他增删改 ======================================= */

    public void updateQuantity(String itemId, int quantity) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("购物车项不存在"));
        item.setQuantity(quantity);
        cartItemRepository.save(item);
    }

    public void deleteItem(String itemId) {
        cartItemRepository.deleteById(itemId);
    }

    public void clearCart(String username, String sessionId) {
        if (username != null) {
            cartItemRepository.deleteByUsername(username);
        } else {
            cartItemRepository.deleteBySessionId(sessionId);
        }
    }

    /* =========================== 购物车 + 菜品 + 餐厅名 =========================== */

    public List<CartItemWithDish> getCartWithDish(String username, String sessionId) {

        List<CartItem> raw = getCart(username, sessionId);
        List<CartItemWithDish> result = new ArrayList<>();

        for (CartItem item : raw) {

            /* 找到 Dish */
            Dish dish = findDishByRestaurantAndDishId(
                    item.getRestaurantId(), item.getDishId()
            ).orElse(null);


            /* 找到餐厅名 */
            String restName = restaurantRepository.findById(
                            Long.valueOf(item.getRestaurantId()))   // ← ① 变成 Long
                    .map(Restaurant::getName)                   //    ② import 用你自己的实体
                    .orElse("未知餐厅");


            result.add(new CartItemWithDish(item, dish, restName));
        }
        return result;
    }

    /* =============== 在菜单里按 “餐厅 + dishId” 搜菜 =============== */

    public Optional<Dish> findDishByRestaurantAndDishId(String restaurantId, String dishId) {
        Menu menu = menuRepository.findByRestaurantId(Integer.parseInt(restaurantId));
        if (menu == null) return Optional.empty();
        return menu.getItems().stream()
                .filter(d -> dishId.equals(d.getId().toString()))
                .findFirst();
    }
}
