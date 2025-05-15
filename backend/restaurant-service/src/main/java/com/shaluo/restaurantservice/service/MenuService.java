// src/main/java/com/shaluo/snapbite/service/MenuService.java
package com.shaluo.restaurantservice.service;

import com.shaluo.restaurantservice.dto.response.DishResponse;
import com.shaluo.restaurantservice.dto.response.MenuResponse;
import com.shaluo.restaurantservice.model.mongo.Dish;
import com.shaluo.restaurantservice.model.mongo.Menu;
import com.shaluo.restaurantservice.repository.mongo.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {


    private final MenuRepository menuRepository;

    // 推荐构造器注入（只有一个构造方法就行）
    @Autowired
    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    // 根据餐厅id，返回餐厅的 MenuResponse 给前端
    public MenuResponse getMenuResponseByRestaurantId(Integer restaurantId) {

        // 找到对应餐厅的菜单实体
        Menu menu = menuRepository.findByRestaurantId(restaurantId);

        // 准备一个空的菜品响应列表
        List<DishResponse> dishes = new ArrayList<>();

        // 遍历原始菜品，逐个转换为 DishResponse 并加入列表
        for (Dish dish : menu.getItems()) {
            Long id = dish.getId();
            String name = dish.getName();
            Double dishPrice = dish.getPrice();
            String description = dish.getDescription();

            DishResponse dto = new DishResponse(id, name, dishPrice, description);
            dishes.add(dto);
        }

        // 构造并 返回对象 MenuResponse
        return new MenuResponse(restaurantId, dishes);
    }



}
