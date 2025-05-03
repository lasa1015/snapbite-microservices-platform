package com.shaluo.snapbite.controller;

import com.shaluo.snapbite.dto.DishSummary;
import com.shaluo.snapbite.model.mongo.Dish;
import com.shaluo.snapbite.model.mongo.Menu;
import com.shaluo.snapbite.repository.mongo.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MenuRepository menuRepository;

    @GetMapping("/restaurant/{id}")
    public Menu getMenuByRestaurantId(@PathVariable("id") Integer restaurantId) {
        return menuRepository.findByRestaurantId(restaurantId);
    }

    /** 返回所有菜的简要信息（给购物车侧边栏用） */
    @GetMapping("/dishes")
    public List<DishSummary> getAllDishes() {
        List<Menu> menus = menuRepository.findAll();
        List<DishSummary> dishes = new ArrayList<>();

        for (Menu menu : menus) {
            for (Dish d : menu.getItems()) {
                /* 旧数据可能没有 id，给它补一个临时 uuid，至少别让接口崩 */
                String dishId = d.getId() != null
                        ? d.getId().toString()
                        : "tmp-" + UUID.randomUUID();

                dishes.add(new DishSummary(dishId, d.getName(), d.getPrice()));
            }
        }
        return dishes;
    }
}
