// src/main/java/com/shaluo/snapbite/service/MenuService.java
package com.shaluo.snapbite.service;

import com.shaluo.snapbite.dto.DishResponse;
import com.shaluo.snapbite.dto.MenuResponse;
import com.shaluo.snapbite.model.mongo.Dish;
import com.shaluo.snapbite.model.mongo.Menu;
import com.shaluo.snapbite.repository.mongo.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;


    // 根据餐厅id，返回餐厅的 MenuResponse 给前端
    public MenuResponse getMenuResponseByRestaurantId(Integer restaurantId) {

        // 找到对应餐厅的菜单实体
        Menu menu = menuRepository.findByRestaurantId(restaurantId);

        // 准备一个空的菜品响应列表
        List<DishResponse> dishes = new ArrayList<>();

        // 遍历原始菜品，逐个转换为 DishResponse 并加入列表
        for (Dish dish : menu.getItems()) {
            String id = dish.getId().toString();
            String name = dish.getName();
            Double price = dish.getPrice();

            DishResponse dto = new DishResponse(id, name, price);
            dishes.add(dto);
        }

        // 构造并 返回对象 MenuResponse
        return new MenuResponse(restaurantId, dishes);
    }



}
