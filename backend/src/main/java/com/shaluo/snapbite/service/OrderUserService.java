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
import java.util.stream.Collectors;

@Service
public class OrderUserService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    public void checkout(String username, CheckoutRequest request) {
        // 1. 查询用户
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 2. 查询购物车
        List<CartItem> cart = cartItemRepository.findByUsername(username);
        if (cart.isEmpty()) throw new RuntimeException("购物车为空");

        // 3. 按 restaurantId 分组
        Map<String, List<CartItem>> groupedByRestaurant = cart.stream()
                .collect(Collectors.groupingBy(CartItem::getRestaurantId));

        for (Map.Entry<String, List<CartItem>> entry : groupedByRestaurant.entrySet()) {
            String restaurantId = entry.getKey();
            List<CartItem> items = entry.getValue();

            // 4. 创建一个订单
            Order order = new Order();
            order.setId(UUID.randomUUID());
            order.setUser(user);
            order.setRecipient(request.getRecipient());
            order.setPhone(request.getPhone());
            order.setAddress(request.getAddress());
            order.setStatus(OrderStatus.CREATED);

            // 5. 查询菜单
            Menu menu = menuRepository.findByRestaurantId(Integer.parseInt(restaurantId));
            if (menu == null) continue;

            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem cartItem : items) {
                Optional<Dish> dishOpt = menu.getItems().stream()
                        .filter(d -> d.getId().toString().equals(cartItem.getDishId()))
                        .findFirst();

                if (dishOpt.isEmpty()) continue;

                Dish dish = dishOpt.get();

                OrderItem orderItem = new OrderItem();
                orderItem.setId(UUID.randomUUID());  //
                orderItem.setOrder(order);
                orderItem.setRestaurantId(restaurantId);
                orderItem.setDishId(cartItem.getDishId());
                orderItem.setDishName(dish.getName());
                orderItem.setPrice(dish.getPrice());
                orderItem.setQuantity(cartItem.getQuantity());

                orderItems.add(orderItem);
            }

            if (!orderItems.isEmpty()) {
                order.setItems(orderItems);
                order.setTotalPrice(calculateTotalAmount(orderItems));
                orderRepository.save(order);
            }
        }

        // 6. 清空购物车
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
                i.setDishId(item.getDishId());

                return i;
            }).toList();

            response.setItems(itemResponses);
            return response;
        }).toList();
    }

    public void cancelOrder(String username, UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!order.getUser().getUsername().equals(username)) {
            throw new RuntimeException("无权限取消该订单");
        }

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new RuntimeException("该订单无法取消");
        }

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    public void confirmOrder(String username, UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!order.getUser().getUsername().equals(username)) {
            throw new RuntimeException("无权限确认该订单");
        }

        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new RuntimeException("当前状态无法确认收货");
        }

        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
    }



}
