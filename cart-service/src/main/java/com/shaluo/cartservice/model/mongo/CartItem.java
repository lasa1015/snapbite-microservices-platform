package com.shaluo.cartservice.model.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "cart_items")  // 这是 MongoDB 的文档对象,存在的集合（类似表）叫 cart_items
public class CartItem {

    // Spring Data MongoDB 会自动为你生成一个全局唯一的 ObjectId（字符串形式）
    // 比如 662a98d0736cf84ee1262c75
    // 这个 ID 实际上就是一种 UUID，它非常适合当作主键来查找文档。
    @Id
    private String id;

    private String username;   // 登录用户

    private String restaurantId;

    private String restaurantName;

    private String dishId;

    private int quantity;

    private LocalDateTime addedAt;

    private String dishName;

    private Double dishPrice;

}
