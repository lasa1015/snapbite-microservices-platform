package com.shaluo.snapbite.service;

import com.shaluo.snapbite.dto.RestaurantFilterRequest;
import com.shaluo.snapbite.dto.RestaurantResponse;
import com.shaluo.snapbite.model.Restaurant;
import com.shaluo.snapbite.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;


@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;


    // 实体 转 DTO
    private RestaurantResponse toResponse(Restaurant r) {
        RestaurantResponse res = new RestaurantResponse();
        res.setId(r.getId());
        res.setName(r.getName());
        res.setImgUrl(r.getImgUrl());
        res.setRating(r.getRating());
        res.setPrice(r.getPrice());
        res.setCategory(r.getCategory());
        res.setDescription(r.getDescription());
        res.setDisplayAddress(r.getAddress());
        return res;
    }



    // 查询所有餐厅
    public List<RestaurantResponse> getAllRestaurants() {

        // 从数据库查出所有餐厅
        List<Restaurant> restaurants = restaurantRepository.findAll();

        // 创建一个空的 List<RestaurantResponse>
        List<RestaurantResponse> responseList = new ArrayList<>();

        // 把每个 Restaurant 转成 RestaurantResponse
        for (Restaurant restaurant : restaurants) {
            RestaurantResponse response = this.toResponse(restaurant);
            responseList.add(response);
        }

        //  返回处理完的列表
        return responseList;
    }



    // 筛选符合条件的餐厅
    public List<RestaurantResponse> filterRestaurants(RestaurantFilterRequest filter) {

        // 根据前端传入的meals是否空，决定 mealFiltersEmpty 是 ture 还是false
        boolean mealFiltersEmpty = (filter.getMeals() == null || filter.getMeals().isEmpty());

        // 调用 Repository 里的 JPQL 查询语句，返回筛选后的餐厅列表。
        List<Restaurant> result = restaurantRepository.filterRestaurants(
                filter.getCategories(),
                filter.getPrices(),
                filter.getMeals(),
                mealFiltersEmpty
        );

        // 在 Java 层进行评分排序， 因为 JPQL 不能动态指定字段名作为排序依据
        // 而且后续可以支持更多排序方式
        if ("asc".equalsIgnoreCase(filter.getSortOrder())) {
            result.sort(Comparator.comparingDouble(Restaurant::getRating));
        }

        else if ("desc".equalsIgnoreCase(filter.getSortOrder())) {
            result.sort(Comparator.comparingDouble(Restaurant::getRating).reversed());
        }

        // 把 List<Restaurant>（实体类）转换成 List<RestaurantResponse>（DTO类），并返回给前端
        return result.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

    }

    // 按照id查询单个餐厅
    public RestaurantResponse getRestaurantById(Long id) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("餐厅不存在"));

        return toResponse(restaurant);
    }

}
