package com.shaluo.snapbite.model.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "menus") // 指定 MongoDB 中的集合名
public class Menu {

    @Id
    private String id; // MongoDB 自动生成的主键

    private Integer restaurantId;

    private List<Dish> items;
}
