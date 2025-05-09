package com.shaluo.orderservice.service;

import com.shaluo.orderservice.dto.request.CheckoutRequest;
import com.shaluo.orderservice.dto.response.OrderItemResponse;
import com.shaluo.orderservice.dto.response.OrderResponse;
import com.shaluo.orderservice.client.CartClient;
import com.shaluo.orderservice.client.UserClient;
import com.shaluo.orderservice.dto.external.RestaurantCartGroup;
import com.shaluo.orderservice.model.postgres.Order;
import com.shaluo.orderservice.model.postgres.OrderItem;
import com.shaluo.orderservice.model.postgres.OrderStatus;
import com.shaluo.orderservice.repository.postgres.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderUserService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartClient cartClient;

    @Autowired
    private UserClient userClient;

    // 【用户下单】
    public void checkout(String username, CheckoutRequest request) {

        // 根据用户名，拉取购物车数据
        List<RestaurantCartGroup> groupedCart = cartClient.getGroupedCart(username);

        if (groupedCart.isEmpty()) throw new RuntimeException("购物车为空");

        // 根据用户名，从 user-service 拉取 userId
        Long userId = userClient.getUserId(username);

        // 遍历购物车数据
        for (RestaurantCartGroup group : groupedCart) {

            // 设置订单属性
            Order order = new Order();

            order.setId(UUID.randomUUID());
            order.setUserId(userId);

            order.setRecipient(request.getRecipient());
            order.setPhone(request.getPhone());
            order.setAddress(request.getAddress());

            order.setStatus(OrderStatus.CREATED);
            order.setRestaurantId(group.getRestaurantId());
            order.setRestaurantName(group.getRestaurantName());

            // 设置每一个orderItem
            List<OrderItem> orderItems = group.getItems().stream().map(item -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setId(UUID.randomUUID());
                orderItem.setOrder(order);
                orderItem.setRestaurantId(group.getRestaurantId());
                orderItem.setDishId(item.getDishId());
                orderItem.setDishName(item.getDishName());
                orderItem.setPrice(item.getPrice());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setSubtotal(item.getPrice() * item.getQuantity());

                return orderItem;
            }).collect(Collectors.toList());

            order.setItems(orderItems);
            order.setTotalPrice(calculateTotalAmount(orderItems));

            orderRepository.save(order);
        }

        cartClient.clearCart(username);
    }


    private double calculateTotalAmount(List<OrderItem> items) {
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }


    //【根据用户名，获取用户所有订单】
    public List<OrderResponse> getUserOrders(String username) {

        // 调用 user-service 获取 userId
        Long userId = userClient.getUserId(username);

        // 从订单中找到所有该用户的，并按照创建顺序倒序
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);

        // 返回体
        List<OrderResponse> responses = new ArrayList<>();

        // 遍历所有的订单实体，构造orderResponse返回体
        for (Order order : orders) {

            OrderResponse response = new OrderResponse();

            response.setId(order.getId().toString());
            response.setAddress(order.getAddress());
            response.setRecipient(order.getRecipient());
            response.setPhone(order.getPhone());
            response.setStatus(order.getStatus().name());
            response.setTotalPrice(order.getTotalPrice());
            response.setCreatedAt(order.getCreatedAt());
            response.setRestaurantId(order.getRestaurantId());
            response.setRestaurantName(order.getRestaurantName());

            List<OrderItemResponse> itemResponses = new ArrayList<>();

            // 遍历所有的OrderItem，构造OrderItemResponse返回体
            for (OrderItem item : order.getItems()) {
                OrderItemResponse i = new OrderItemResponse();
                i.setDishName(item.getDishName());
                i.setPrice(item.getPrice());
                i.setQuantity(item.getQuantity());
                i.setRestaurantId(item.getRestaurantId());
                i.setDishId(item.getDishId());
                i.setSubtotal(item.getSubtotal());

                itemResponses.add(i);
            }

            response.setItems(itemResponses);
            responses.add(response);
        }

        return responses;

    }

    //  用户取消订单
    public void cancelOrder(String username, UUID orderId) {

        // 调用 user-service 获取 userId
        Long userId = userClient.getUserId(username);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        // 只有 order.userId 和 当前登录用户的 userId 相同，才能取消订单
        if (!Objects.equals(order.getUserId(), userId)) {
            throw new RuntimeException("无权限取消该订单");
        }

        // 只有还是CREATED 或 ACCEPTED 状态的订单，才能够取消
        if (order.getStatus() != OrderStatus.CREATED && order.getStatus() != OrderStatus.ACCEPTED) {
            throw new RuntimeException("该订单当前状态无法取消");
        }

        // 修改订单状态（改内存中的值）
        order.setStatus(OrderStatus.CANCELED);

        // 写入数据库
        orderRepository.save(order);
    }



    // 确认收货
    public void confirmOrder(String username, UUID orderId) {

        // 调用 user-service 获取 userId
        Long userId = userClient.getUserId(username);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!Objects.equals(order.getUserId(), userId)) {
            throw new RuntimeException("无权限确认该订单");
        }

        // 只有订单在SHIPPED状态才能确认收货
        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new RuntimeException("当前状态无法确认收货");
        }

        order.setStatus(OrderStatus.COMPLETED);

        orderRepository.save(order);
    }

}
