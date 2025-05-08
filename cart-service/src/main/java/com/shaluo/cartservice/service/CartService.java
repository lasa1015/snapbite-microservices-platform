package com.shaluo.cartservice.service;

import com.shaluo.cartservice.dto.AddCartItemRequest;
import com.shaluo.cartservice.dto.CartItemResponse;
import com.shaluo.cartservice.dto.RestaurantCartGroup;
import com.shaluo.cartservice.model.mongo.CartItem;

import com.shaluo.cartservice.repository.mongo.CartItemRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;


    // 获取当前登录用户的购物车，每一项都带有“菜品名”和“餐厅名”等展示信息。
    public List<RestaurantCartGroup> getGroupedCart(String username) {

        //  根据用户名查出所有购物车项
        List<CartItem> raw = cartItemRepository.findByUsername(username);

        // 用 Map 按照 restaurantId 进行分组
        Map<String, List<CartItem>> groupedMap = new HashMap<>();
        for (CartItem item : raw) {

            // 如果该餐厅还没有创建对应的列表，就先初始化
            groupedMap
                    .computeIfAbsent(item.getRestaurantId(), k -> new ArrayList<>())
                    .add(item);
        }

        // 构造最终返回结构
        List<RestaurantCartGroup> result = new ArrayList<>();

        // 遍历每个餐厅的购物车项列表
        for (Map.Entry<String, List<CartItem>> entry : groupedMap.entrySet()) {

            String restId = entry.getKey();           // 餐厅ID
            List<CartItem> items = entry.getValue();  // 该餐厅的所有购物车项

            // 取第一项的餐厅名（同一组内都一样）
            String restName = items.get(0).getRestaurantName();

            // 构造该餐厅下的菜品列表
            List<CartItemResponse> itemResponses = new ArrayList<>();

            for (CartItem item : items) {

                itemResponses.add(new CartItemResponse(
                        item.getId(),
                        item.getDishId(),
                        item.getDishName(),
                        item.getDishPrice(),
                        item.getQuantity()
                ));
            }

            // 加入最终结果列表中
            result.add(new RestaurantCartGroup(restId, restName, itemResponses));
        }
        return result;
    }


    // 为某个用户向购物车添加菜品，如果已有相同菜品则累加数量，否则新增条目。
    public CartItem addOrUpdate(String username, AddCartItemRequest req) {

        // 根据 getRestaurantId、getDishId和username，查看这个菜是否已经在购物车里了
        Optional<CartItem> existing = cartItemRepository
                .findByUsernameAndRestaurantIdAndDishId(username, req.getRestaurantId(), req.getDishId());


        CartItem item = existing.orElse(new CartItem());

        // 设置属性
        item.setUsername(username);
        item.setDishId(req.getDishId());
        item.setDishName(req.getDishName());
        item.setDishPrice(req.getDishPrice());
        item.setRestaurantId(req.getRestaurantId());
        item.setRestaurantName(req.getRestaurantName());

        item.setQuantity(item.getId() == null ? req.getQuantity() : item.getQuantity() + req.getQuantity());
        item.setAddedAt(LocalDateTime.now());

        return cartItemRepository.save(item);
    }



    // 根据购物车项的唯一 ID 更新该项的数量
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
