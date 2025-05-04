// src/main/java/com/shaluo/snapbite/service/MenuService.java
package com.shaluo.snapbite.service;

import com.shaluo.snapbite.model.mongo.Dish;
import com.shaluo.snapbite.model.mongo.Menu;
import com.shaluo.snapbite.repository.mongo.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    public Optional<Dish> findDishByRestaurantAndDishId(String restaurantId, String dishId) {
        Menu menu = menuRepository.findByRestaurantId(Integer.parseInt(restaurantId));
        if (menu == null) return Optional.empty();
        return menu.getItems().stream()
                .filter(d -> dishId.equals(d.getId().toString()))
                .findFirst();
    }
}
