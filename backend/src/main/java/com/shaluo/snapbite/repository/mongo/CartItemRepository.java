package com.shaluo.snapbite.repository.mongo;

import com.shaluo.snapbite.model.mongo.CartItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends MongoRepository<CartItem, String> {

    /* 查询购物车 */
    List<CartItem> findByUsername(String username);
    List<CartItem> findBySessionId(String sessionId);

    /* === 关键：同时用 restaurantId + dishId === */
    Optional<CartItem> findByUsernameAndRestaurantIdAndDishId(String username,
                                                              String restaurantId,
                                                              String dishId);

    Optional<CartItem> findBySessionIdAndRestaurantIdAndDishId(String sessionId,
                                                               String restaurantId,
                                                               String dishId);

    /* 批量删除 */
    void deleteByUsername(String username);
    void deleteBySessionId(String sessionId);
}
