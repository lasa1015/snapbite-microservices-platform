package com.shaluo.restaurantservice.controller;

// 引入 Restaurant 实体类
import com.shaluo.restaurantservice.dto.RestaurantFilterRequest;
import com.shaluo.restaurantservice.dto.RestaurantResponse;

// 引入对应的 JPA 仓库接口

import com.shaluo.restaurantservice.repository.postgres.RestaurantRepository;
import com.shaluo.restaurantservice.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 表示这是一个 REST 控制器，返回的是 JSON 数据而不是页面
@RequestMapping("/api/restaurants") // 设置路由前缀：所有接口都以 /api/restaurants 开头
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RestaurantRepository restaurantRepository;


    // 获取所有餐馆
    @GetMapping("/all") // 对应 GET 请求（无参数），即 GET /api/restaurants
    public List<RestaurantResponse> getAllRestaurants() {

        return restaurantService.getAllRestaurants();
    }

    // 根据前端传入的filter，筛选出符合条件的餐厅
    @PostMapping("/filter")
    public List<RestaurantResponse> filterRestaurants(@RequestBody RestaurantFilterRequest filter) {

        return restaurantService.filterRestaurants(filter);
    }

    // 按照id查询单个餐厅
    @PostMapping("/{id}")
    public RestaurantResponse getRestaurantById(@PathVariable Long id) {
        return restaurantService.getRestaurantByOwnerId(id);
    }

    // 根据商家的username，查询对应餐厅的 id
    @GetMapping("/api/restaurant/owner/{username}")
    public String getRestaurantIdByOwnerUsername(@PathVariable String username) {

        return restaurantRepository.findByName(username)
                .orElseThrow(() -> new RuntimeException("该商户未绑定餐厅"))
                .getId().toString();
    }


}
