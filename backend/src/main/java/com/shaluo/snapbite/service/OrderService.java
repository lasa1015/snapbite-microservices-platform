package com.shaluo.snapbite.service;

import com.shaluo.snapbite.dto.CheckoutRequest;
import com.shaluo.snapbite.dto.OrderItemResponse;
import com.shaluo.snapbite.dto.OrderResponse;
import com.shaluo.snapbite.model.mongo.CartItem;
import com.shaluo.snapbite.model.mongo.Dish;
import com.shaluo.snapbite.model.mongo.Menu;
import com.shaluo.snapbite.model.postgres.Order;
import com.shaluo.snapbite.model.postgres.OrderItem;
import com.shaluo.snapbite.model.postgres.OrderStatus;
import com.shaluo.snapbite.model.postgres.User;
import com.shaluo.snapbite.repository.mongo.CartItemRepository;
import com.shaluo.snapbite.repository.mongo.MenuRepository;
import com.shaluo.snapbite.repository.postgres.OrderRepository;
import com.shaluo.snapbite.repository.postgres.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    public void checkout(String username, CheckoutRequest request) {
        // 查询用户
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 查购物车
        List<CartItem> cart = cartItemRepository.findByUsername(username);
        if (cart.isEmpty()) throw new RuntimeException("购物车为空，无法下单");

        // 创建订单
        Order order = new Order();
        order.setId(UUID.randomUUID());  // 👈 新增这句
        order.setUser(user); // 用对象设置外键
        order.setRecipient(request.getRecipient());
        order.setPhone(request.getPhone());
        order.setAddress(request.getAddress());
        order.setStatus(OrderStatus.CREATED);

        List<OrderItem> items = new ArrayList<>();
        for (CartItem cartItem : cart) {
            Menu menu = menuRepository.findByRestaurantId(Integer.parseInt(cartItem.getRestaurantId()));
            if (menu == null) continue;

            Optional<Dish> dishOpt = menu.getItems().stream()
                    .filter(d -> cartItem.getDishId().equals(d.getId().toString()))
                    .findFirst();

            if (dishOpt.isEmpty()) continue;

            Dish dish = dishOpt.get();

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setRestaurantId(cartItem.getRestaurantId());
            orderItem.setDishId(cartItem.getDishId());
            orderItem.setDishName(dish.getName());
            orderItem.setPrice(dish.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());

            items.add(orderItem);
        }

        if (items.isEmpty()) throw new RuntimeException("菜品信息异常，无法创建订单");

        order.setItems(items);
        order.setTotalPrice(calculateTotalAmount(items)); // 设置总价
        orderRepository.save(order);
        cartItemRepository.deleteByUsername(username);
    }

    private double calculateTotalAmount(List<OrderItem> items) {
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }


    public List<OrderResponse> getUserOrders(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        List<Order> orders = orderRepository.findByUserOrderByCreatedAtDesc(user);

        return orders.stream().map(order -> {
            OrderResponse response = new OrderResponse();
            response.setId(order.getId().toString());
            response.setAddress(order.getAddress());
            response.setRecipient(order.getRecipient());
            response.setPhone(order.getPhone());
            response.setStatus(order.getStatus().name());
            response.setTotalPrice(order.getTotalPrice());
            response.setCreatedAt(order.getCreatedAt());

            List<OrderItemResponse> itemResponses = order.getItems().stream().map(item -> {
                OrderItemResponse i = new OrderItemResponse();
                i.setDishName(item.getDishName());
                i.setPrice(item.getPrice());
                i.setQuantity(item.getQuantity());
                i.setRestaurantId(item.getRestaurantId());
                return i;
            }).toList();

            response.setItems(itemResponses);
            return response;
        }).toList();
    }

}
