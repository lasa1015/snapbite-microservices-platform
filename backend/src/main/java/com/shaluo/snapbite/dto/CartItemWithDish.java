package com.shaluo.snapbite.dto;

import com.shaluo.snapbite.model.mongo.CartItem;
import com.shaluo.snapbite.model.mongo.Dish;
import lombok.Data;

@Data
public class CartItemWithDish {

    private String id;
    private String dishId;
    private String restaurantId;
    private int quantity;

    // 显示字段
    private String restaurantName;
    private String dishName;
    private Double price;

    public CartItemWithDish(CartItem item, Dish dish, String restaurantName) {
        this.id            = item.getId();
        this.dishId        = item.getDishId();
        this.restaurantId  = item.getRestaurantId();
        this.quantity      = item.getQuantity();

        this.restaurantName = restaurantName;
        this.dishName       = dish != null ? dish.getName()  : null;
        this.price          = dish != null ? dish.getPrice() : null;
    }
}
