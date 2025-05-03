package com.shaluo.snapbite.service;

import com.shaluo.snapbite.dto.RestaurantFilterRequest;
import com.shaluo.snapbite.dto.RestaurantResponse;
import com.shaluo.snapbite.model.Restaurant;
import com.shaluo.snapbite.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    // 查询所有餐厅
    public List<RestaurantResponse> getAllRestaurants() {
        return restaurantRepository.findAll().stream().map(this::toResponse).toList();
    }

    // 筛选餐厅
    public List<RestaurantResponse> filterRestaurants(RestaurantFilterRequest filter) {

        boolean mealFiltersEmpty = (filter.getMeals() == null || filter.getMeals().isEmpty());

        List<Restaurant> list = restaurantRepository.filterRestaurants(
                filter.getCategories(),
                filter.getPrices(),
                filter.getMeals(),
                mealFiltersEmpty
        );
        return list.stream().map(this::toResponse).toList();
    }

    // 实体转 DTO RestaurantResponse
    private RestaurantResponse toResponse(Restaurant r) {
        RestaurantResponse res = new RestaurantResponse();
        res.setId(r.getId());
        res.setName(r.getName());
        res.setImgUrl(r.getImgUrl());
        res.setRating(r.getRating());
        res.setPrice(r.getPrice());
        res.setCategory(r.getCategory());
        res.setDescription(r.getDescription());
        res.setReviewCount(100 + (int) (Math.random() * 100)); // 示例
        res.setDisplayAddress(r.getAddress());
        return res;
    }
}
