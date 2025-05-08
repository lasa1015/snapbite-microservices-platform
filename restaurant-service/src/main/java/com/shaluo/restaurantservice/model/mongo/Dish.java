package com.shaluo.restaurantservice.model.mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

// 这是一个嵌套文档类（Embedded Document），在 MongoDB 项目中很常见
// Dish 并不是 MongoDB 顶层集合里的文档（collection），而是 Menu 文档内部嵌套的结构
// 不需要注解 @Document，不需要 Repository
@Data
public class Dish {

    @Field("id")
    private Long id;

    private String name;
    private String description;
    private Double price;
}