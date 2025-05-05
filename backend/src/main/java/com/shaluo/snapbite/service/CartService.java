package com.shaluo.snapbite.service;

import com.shaluo.snapbite.dto.CartItemDetailResponse;

import com.shaluo.snapbite.model.mongo.CartItem;
import com.shaluo.snapbite.model.mongo.Dish;
import com.shaluo.snapbite.model.mongo.Menu;
import com.shaluo.snapbite.repository.mongo.CartItemRepository;
import com.shaluo.snapbite.repository.mongo.MenuRepository;
import com.shaluo.snapbite.repository.postgres.RestaurantRepository;
import com.shaluo.snapbite.model.postgres.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    // 获取当前登录用户的购物车，每一项都带有“菜品名”和“餐厅名”等展示信息。
    public List<CartItemDetailResponse> getCartWithDish(String username) {

        // 按照username，查找所有购物车项目
        List<CartItem> raw = cartItemRepository.findByUsername(username);

        // 准备一个结果列表
        List<CartItemDetailResponse> result = new ArrayList<>();

        // 遍历
        for (CartItem item : raw) {

            // 根据购物车项目中的 RestaurantId 和 DishId， 查询到对应的 Dish
            Dish dish = findDishByRestaurantAndDishId(item.getRestaurantId(), item.getDishId()).orElse(null);

            // 根据购物车项目中的 RestaurantId ，查询到对应的 餐厅的名字
            String restName = restaurantRepository.findById(Long.valueOf(item.getRestaurantId()))
                    .map(Restaurant::getName).orElse("未知餐厅");

            // 组装 DTO 对象并加入结果列表
            result.add(new CartItemDetailResponse(item, dish, restName));
        }

        return result;
    }

    // 根据餐厅 ID 和菜品 ID，从 MongoDB 中的 Menu 文档中找到对应的 Dish 对象
    public Optional<Dish> findDishByRestaurantAndDishId(String restaurantId, String dishId) {

        // 通过餐厅 ID 查询该餐厅的菜单
        Menu menu = menuRepository.findByRestaurantId(Integer.parseInt(restaurantId));

        if (menu == null) return Optional.empty();

        // 从菜单中，找到指定 dishId 对应的菜品
        return menu.getItems().stream()
                .filter(d -> dishId.equals(d.getId().toString()))
                .findFirst();
    }

    // 为某个用户向购物车添加菜品，如果已有相同菜品则累加数量，否则新增条目。
    public CartItem addOrUpdate(String username,
                                String dishId,
                                String restaurantId,
                                int quantity) {

        // 查找这个用户是否已经在购物车中添加了相同餐厅+相同菜品
        Optional<CartItem> existing = cartItemRepository
                .findByUsernameAndRestaurantIdAndDishId(username, restaurantId, dishId);

        // 构造或复用已有购物车项
        CartItem item = existing.orElse(new CartItem());

        // 设置各字段
        item.setUsername(username);
        item.setDishId(dishId);
        item.setRestaurantId(restaurantId);

        // 如果这是新建项（没有 ID） → 直接设置 quantity = 添加的数量；
        // 如果是已有项 → 在原有数量基础上加上新数量；
        item.setQuantity(item.getId() == null ? quantity : item.getQuantity() + quantity);
        item.setAddedAt(LocalDateTime.now());

        // 保存到 MongoDB
        return cartItemRepository.save(item);
    }


    // 根据购物车项 ID 更新该项的数量
    public void updateQuantity(String id, int quantity) {

        // 查找该购物车项
        CartItem item = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("购物车项不存在"));

        // 更新数量
        item.setQuantity(quantity);

        cartItemRepository.save(item);
    }


    // 根据id，删除购物车中的某个条目
    public void deleteItem(String id) {

        cartItemRepository.deleteById(id);
    }


    // 根据 username 清空购物车
    public void clearCart(String username) {

        cartItemRepository.deleteByUsername(username);
    }




}
