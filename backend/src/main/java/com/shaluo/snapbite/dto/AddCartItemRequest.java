package com.shaluo.snapbite.dto;

import lombok.Data;

// 接收前端“添加到购物车”请求时发过来的数据
// 比如用户点击“加入购物车”，前端会 POST 一个 JSON
//   {
//        "dishId": "5",
//        "restaurantId": "11",
//        "quantity": 2
//    }


@Data
public class AddCartItemRequest {

    private String dishId;

    private String restaurantId;

    private int quantity;
}
