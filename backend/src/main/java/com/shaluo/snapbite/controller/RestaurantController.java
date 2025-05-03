package com.shaluo.snapbite.controller;

// 引入 Restaurant 实体类
import com.shaluo.snapbite.dto.RestaurantFilterRequest;
import com.shaluo.snapbite.model.Restaurant;

// 引入对应的 JPA 仓库接口
import com.shaluo.snapbite.repository.RestaurantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 表示这是一个 REST 控制器，返回的是 JSON 数据而不是页面
@RequestMapping("/api/restaurants") // 设置路由前缀：所有接口都以 /api/restaurants 开头
public class RestaurantController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping // 对应 GET 请求（无参数），即 GET /api/restaurants
    public List<Restaurant> getAllRestaurants() {

        // 查询数据库中所有餐馆并返回（作为 JSON 返回到前端）
        return restaurantRepository.findAll();
    }

    // 筛选接口
    @PostMapping("/filter")
    public List<Restaurant> filterRestaurants(@RequestBody RestaurantFilterRequest filter) {
        boolean mealFiltersEmpty = (filter.getMeals() == null || filter.getMeals().isEmpty());
        return restaurantRepository.filterRestaurants(
                filter.getCategories(),
                filter.getPrices(),
                filter.getMeals(),
                mealFiltersEmpty
        );
    }
}
