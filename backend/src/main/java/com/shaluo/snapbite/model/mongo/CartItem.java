package com.shaluo.snapbite.model.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "cart_items")
public class CartItem {

    @Id
    private String id;

    private String username;   // 登录用户用
    private String sessionId;  // 游客用户用

    private String dishId;
    private String restaurantId;
    private int quantity;

    private LocalDateTime addedAt;
}
