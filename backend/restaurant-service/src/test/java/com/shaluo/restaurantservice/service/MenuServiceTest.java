package com.shaluo.restaurantservice.service;

import com.shaluo.restaurantservice.dto.response.DishResponse;
import com.shaluo.restaurantservice.dto.response.MenuResponse;
import com.shaluo.restaurantservice.model.mongo.Dish;
import com.shaluo.restaurantservice.model.mongo.Menu;
import com.shaluo.restaurantservice.repository.mongo.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuServiceTest {

    private MenuRepository menuRepository;
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuRepository = mock(MenuRepository.class);
        // 直接构造注入，不需要set方法，也不用访问private字段
        menuService = new MenuService(menuRepository);
    }


    @Test
    void testGetMenuResponseByRestaurantId_shouldReturnCorrectData() {

        // 构造假数据：两个菜品
        Dish dish1 = new Dish(1L, "Fried Rice", "Classic egg fried rice", 8.5);
        Dish dish2 = new Dish(2L, "Noodles", "Spicy beef noodles", 9.0);
        List<Dish> dishList = Arrays.asList(dish1, dish2);

        // 构造菜单对象，绑定到某个餐厅
        Menu fakeMenu = new Menu(null, 123, dishList); // ✅ 加上 id = null，MongoDB 会自动生成


        // 当调用 menuRepository.findByRestaurantId(123) 时，返回 fakeMenu
        when(menuRepository.findByRestaurantId(123)).thenReturn(fakeMenu);

        // 调用待测方法
        MenuResponse response = menuService.getMenuResponseByRestaurantId(123);

        // 验证返回结果不是 null
        assertNotNull(response);

        // 验证餐厅 ID 一致
        assertEquals(123, response.getRestaurantId());

        // 验证菜品数量
        assertEquals(2, response.getDishes().size());

        // 验证第一个菜品内容
        DishResponse firstDish = response.getDishes().get(0);
        assertEquals("Fried Rice", firstDish.getName());
        assertEquals(8.5, firstDish.getDishPrice());
        assertEquals("Classic egg fried rice", firstDish.getDescription());

        // 验证 menuRepository 的调用是否发生过（可选）
        verify(menuRepository, times(1)).findByRestaurantId(123);
    }
}
