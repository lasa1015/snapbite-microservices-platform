package com.shaluo.restaurantservice.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "menus") // 这是 MongoDB 的文档对象,存在的集合（类似表）叫 menus
public class Menu {

    @Id
    private String id; // MongoDB 自动生成的主键

    private Integer restaurantId;

    private List<Dish> items;
}
