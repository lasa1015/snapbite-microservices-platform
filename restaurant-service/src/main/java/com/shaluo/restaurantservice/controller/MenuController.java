package com.shaluo.restaurantservice.controller;

import com.shaluo.restaurantservice.service.MenuService;
import com.shaluo.restaurantservice.dto.MenuResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
