package com.shaluo.cartservice.service;

import com.shaluo.cartservice.dto.request.AddCartItemRequest;
import com.shaluo.cartservice.dto.response.RestaurantCartGroup;
import com.shaluo.cartservice.model.mongo.CartItem;
import com.shaluo.cartservice.repository.mongo.CartItemRepository;
import com.shaluo.cartservice.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    private CartItemRepository cartItemRepository;
    private CartService cartService;


    @BeforeEach
    void setUp() {
        cartItemRepository = mock(CartItemRepository.class);
        cartService = new CartService(cartItemRepository); // ✅ 用构造器注入
    }


    @Test
    void testGetGroupedCart_shouldGroupByRestaurant() {
        String username = "john";
        List<CartItem> mockItems = List.of(
                createItem("1", username, "R1", "Pizza Palace", "D1", "Margherita", 9.99, 2),
                createItem("2", username, "R1", "Pizza Palace", "D2", "Pepperoni", 11.99, 1),
                createItem("3", username, "R2", "Burger Hub", "D3", "Cheeseburger", 8.99, 3)
        );

        when(cartItemRepository.findByUsername(username)).thenReturn(mockItems);

        List<RestaurantCartGroup> result = cartService.getGroupedCart(username);

        // ✅ 不再断言顺序，只断言内容是否存在
        assertEquals(2, result.size());

        List<String> restaurantNames = result.stream()
                .map(RestaurantCartGroup::getRestaurantName)
                .toList();

        assertTrue(restaurantNames.contains("Pizza Palace"));
        assertTrue(restaurantNames.contains("Burger Hub"));
    }

    @Test
    void testAddOrUpdate_shouldCreateNewItem() {
        String username = "john";
        AddCartItemRequest req = new AddCartItemRequest();
        req.setDishId("D1");
        req.setDishName("Margherita");
        req.setDishPrice(9.99);
        req.setQuantity(2);
        req.setRestaurantId("R1");
        req.setRestaurantName("Pizza Palace");

        when(cartItemRepository.findByUsernameAndRestaurantIdAndDishId(username, "R1", "D1"))
                .thenReturn(Optional.empty());

        when(cartItemRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var saved = cartService.addOrUpdate(username, req);

        assertEquals("D1", saved.getDishId());
        assertEquals(2, saved.getQuantity());
        assertEquals("Pizza Palace", saved.getRestaurantName());
    }

    @Test
    void testAddOrUpdate_shouldUpdateQuantity() {
        String username = "john";
        CartItem existing = createItem("1", username, "R1", "Pizza Palace", "D1", "Margherita", 9.99, 1);

        AddCartItemRequest req = new AddCartItemRequest();
        req.setDishId("D1");
        req.setDishName("Margherita");
        req.setDishPrice(9.99);
        req.setQuantity(2);
        req.setRestaurantId("R1");
        req.setRestaurantName("Pizza Palace");

        when(cartItemRepository.findByUsernameAndRestaurantIdAndDishId(username, "R1", "D1"))
                .thenReturn(Optional.of(existing));

        when(cartItemRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var updated = cartService.addOrUpdate(username, req);
        assertEquals(3, updated.getQuantity());
    }

    @Test
    void testUpdateQuantity_success() {
        CartItem existing = createItem("1", "john", "R1", "Pizza Palace", "D1", "Margherita", 9.99, 1);
        when(cartItemRepository.findById("1")).thenReturn(Optional.of(existing));

        cartService.updateQuantity("1", 5);

        assertEquals(5, existing.getQuantity());
        verify(cartItemRepository, times(1)).save(existing);
    }

    @Test
    void testUpdateQuantity_shouldThrowIfNotFound() {
        when(cartItemRepository.findById("999")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                cartService.updateQuantity("999", 2));

        assertEquals("购物车项不存在", ex.getMessage());
    }

    @Test
    void testDeleteItem_shouldCallRepository() {
        cartService.deleteItem("abc123");
        verify(cartItemRepository, times(1)).deleteById("abc123");
    }

    @Test
    void testClearCart_shouldCallRepository() {
        cartService.clearCart("john");
        verify(cartItemRepository, times(1)).deleteByUsername("john");
    }

    // 🔧 工具方法，避免每次重复写 item 构造
    private CartItem createItem(String id, String username, String restaurantId, String restaurantName,
                                String dishId, String dishName, Double dishPrice, int quantity) {
        CartItem item = new CartItem();
        item.setId(id);
        item.setUsername(username);
        item.setRestaurantId(restaurantId);
        item.setRestaurantName(restaurantName);
        item.setDishId(dishId);
        item.setDishName(dishName);
        item.setDishPrice(dishPrice);
        item.setQuantity(quantity);
        item.setAddedAt(LocalDateTime.now());
        return item;
    }
}
