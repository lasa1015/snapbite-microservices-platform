// dto/RestaurantCartGroup.java
package com.shaluo.cartservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RestaurantCartGroup {
    private String restaurantId;
    private String restaurantName;
    private List<CartItemResponse> items;
}

/*
返回值范例

[
  {
    "restaurantId": "1",
    "restaurantName": "Sushi House",
    "items": [
      {
        "id": "cartItem123",
        "dishId": "10",
        "dishName": "Salmon Nigiri",
        "price": 5.99,
        "quantity": 2
      },
      {
        "id": "cartItem124",
        "dishId": "12",
        "dishName": "Tuna Roll",
        "price": 6.49,
        "quantity": 1
      }
    ]
  },
  {
    "restaurantId": "2",
    "restaurantName": "Pizza Planet",
    "items": [
      {
        "id": "cartItem125",
        "dishId": "22",
        "dishName": "Margherita Pizza",
        "price": 8.99,
        "quantity": 1
      },
      {
        "id": "cartItem126",
        "dishId": "23",
        "dishName": "Pepperoni Pizza",
        "price": 9.99,
        "quantity": 3
      }
    ]
  }
]


 */