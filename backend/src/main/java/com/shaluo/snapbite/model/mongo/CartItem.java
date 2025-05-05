package com.shaluo.snapbite.model.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "cart_items")  // 这是 MongoDB 的文档对象,存在的集合（类似表）叫 cart_items
public class CartItem {

    @Id
    private String id;

    private String username;   // 登录用户

    private String restaurantId;

    private String dishId;

    private int quantity;

    private LocalDateTime addedAt;
}
