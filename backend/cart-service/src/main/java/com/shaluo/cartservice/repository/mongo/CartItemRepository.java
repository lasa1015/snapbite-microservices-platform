package com.shaluo.cartservice.repository.mongo;

import com.shaluo.cartservice.model.mongo.CartItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends MongoRepository<CartItem, String> {

    // 查询某个用户的所有购物车项
    List<CartItem> findByUsername(String username);

    // 根据用户名 + 餐厅ID + 菜品ID 查询用户的某个具体商品
    // 用于添加商品时判断是否已有
    Optional<CartItem> findByUsernameAndRestaurantIdAndDishId(String username,
                                                              String restaurantId,
                                                              String dishId);
    // 清空某个用户的购物车
    void deleteByUsername(String username);
}