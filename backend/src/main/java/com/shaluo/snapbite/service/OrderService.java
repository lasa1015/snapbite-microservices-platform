package com.shaluo.snapbite.service;

import com.shaluo.snapbite.dto.CheckoutRequest;
import com.shaluo.snapbite.model.mongo.CartItem;
import com.shaluo.snapbite.model.mongo.Dish;
import com.shaluo.snapbite.model.mongo.Menu;
import com.shaluo.snapbite.model.postgres.Order;
import com.shaluo.snapbite.model.postgres.OrderItem;
import com.shaluo.snapbite.repository.mongo.CartItemRepository;
import com.shaluo.snapbite.repository.mongo.MenuRepository;
import com.shaluo.snapbite.repository.postgres.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    public void checkout(String username, CheckoutRequest request)
 {
        // 1. 查找购物车
        List<CartItem> cart = cartItemRepository.findByUsername(username);
        if (cart.isEmpty()) {
            throw new RuntimeException("购物车为空，无法下单");
        }

        // 2. 构造订单
        Order order = new Order();
        order.setUsername(username);
        order.setRecipient(request.getRecipient());
        order.setPhone(request.getPhone());
        order.setAddress(request.getAddress());



        List<OrderItem> items = new ArrayList<>();

        for (CartItem cartItem : cart) {
            // 通过 restaurantId 查找菜单
            Menu menu = menuRepository.findByRestaurantId(Integer.parseInt(cartItem.getRestaurantId()));
            if (menu == null) continue;

            Optional<Dish> dishOpt = menu.getItems().stream()
                    .filter(d -> cartItem.getDishId().equals(d.getId().toString()))
                    .findFirst();

            if (dishOpt.isEmpty()) continue;

            Dish dish = dishOpt.get();

            // 构造 OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setDishId(cartItem.getDishId());
            orderItem.setDishName(dish.getName());
            orderItem.setPrice(dish.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setRestaurantId(cartItem.getRestaurantId());

            items.add(orderItem);
        }

        if (items.isEmpty()) {
            throw new RuntimeException("菜品信息异常，无法创建订单");
        }

        order.setItems(items);

        // 3. 保存订单
        orderRepository.save(order);

        // 4. 清空购物车
        cartItemRepository.deleteByUsername(username);
    }
}
