package com.shaluo.snapbite.controller;



import com.shaluo.snapbite.model.mongo.Menu;
import com.shaluo.snapbite.repository.mongo.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MenuRepository menuRepository;

    @GetMapping("/restaurant/{id}")
    public Menu getMenuByRestaurantId(@PathVariable("id")  Integer restaurantId) {
        System.out.println(">>> querying menu for " + restaurantId);
        Menu m = menuRepository.findByRestaurantId(restaurantId);
        System.out.println(">>> result = " + m);
        return m;
    }

}
