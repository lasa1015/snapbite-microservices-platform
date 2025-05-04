package com.shaluo.snapbite.controller;

import com.shaluo.snapbite.dto.DishResponse;
import com.shaluo.snapbite.dto.MenuResponse;
import com.shaluo.snapbite.model.mongo.Dish;
import com.shaluo.snapbite.model.mongo.Menu;
import com.shaluo.snapbite.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    // 根据餐厅 ID 获取菜单
    @GetMapping("/restaurant/{id}")
    public MenuResponse getMenuByRestaurantId(@PathVariable("id") Integer restaurantId) {

        return menuService.getMenuResponseByRestaurantId(restaurantId);
    }

}
