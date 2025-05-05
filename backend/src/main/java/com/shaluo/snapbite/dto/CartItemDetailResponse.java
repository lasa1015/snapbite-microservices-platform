package com.shaluo.snapbite.dto;

import com.shaluo.snapbite.model.mongo.CartItem;
import com.shaluo.snapbite.model.mongo.Dish;
import lombok.Data;

@Data
public class CartItemDetailResponse {

    private String id;
    private String dishId;
    private String restaurantId;
    private int quantity;

    // 显示字段
    private String restaurantName;
    private String dishName;
    private Double price;

    // 对象解构 + 显式组装
    public CartItemDetailResponse(CartItem cartItem, Dish dish, String restaurantName) {

        // 从 cartItem 解构
        this.id            = cartItem.getId();
        this.dishId        = cartItem.getDishId();
        this.restaurantId  = cartItem.getRestaurantId();
        this.quantity      = cartItem.getQuantity();

        // 显式传入
        this.restaurantName = restaurantName;

        // 假设你在某条购物车项中 CartItem 记录了菜品 ID；
        // 但是这道菜在数据库中 被删除了。你查询 Dish 的时候，返回了 null。
        // 如果不进行判断，调用 dish.getName() 程序就会报错
        this.dishName       = dish != null ? dish.getName()  : null;
        this.price          = dish != null ? dish.getPrice() : null;
    }
}
