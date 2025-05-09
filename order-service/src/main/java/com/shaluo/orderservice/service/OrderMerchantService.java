package com.shaluo.orderservice.service;

import com.shaluo.orderservice.client.RestaurantClient;
import com.shaluo.orderservice.dto.response.OrderItemResponse;
import com.shaluo.orderservice.dto.response.OrderResponse;
import com.shaluo.orderservice.model.postgres.Order;
import com.shaluo.orderservice.model.postgres.OrderItem;
import com.shaluo.orderservice.model.postgres.OrderStatus;
import com.shaluo.orderservice.repository.postgres.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderMerchantService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestaurantClient restaurantClient;

    // 获取该商户username下对应餐厅的所有订单
    public List<OrderResponse> getMyRestaurantOrders(String merchantUsername) {

        //  通过 restaurant-service（Feign）获取商户名下的餐厅 ID
        String restaurantId = restaurantClient.getRestaurantIdByOwnerUsername(merchantUsername);

        //  查询该餐厅下的所有订单
        List<Order> orders = orderRepository.findOrdersByRestaurantId(restaurantId);

        //  准备返回值列表（存放转换后的 OrderResponse）
        List<OrderResponse> result = new ArrayList<>();

        // 遍历每一个订单，进行手动转换
        for (Order order : orders) {
            OrderResponse response = new OrderResponse();

            // 设置订单基本信息
            response.setId(order.getId().toString());
            response.setAddress(order.getAddress());
            response.setRecipient(order.getRecipient());
            response.setPhone(order.getPhone());
            response.setStatus(order.getStatus().name());
            response.setTotalPrice(order.getTotalPrice());
            response.setCreatedAt(order.getCreatedAt());

            // 设置餐厅信息
            response.setRestaurantId(order.getRestaurantId());
            response.setRestaurantName(order.getRestaurantName());

            // 构造订单项（菜品列表）
            List<OrderItemResponse> itemResponses = new ArrayList<>();
            for (OrderItem item : order.getItems()) {
                OrderItemResponse itemResponse = new OrderItemResponse();
                itemResponse.setDishName(item.getDishName());
                itemResponse.setPrice(item.getPrice());
                itemResponse.setQuantity(item.getQuantity());
                itemResponse.setRestaurantId(item.getRestaurantId());
                itemResponse.setDishId(item.getDishId());
                itemResponse.setSubtotal(item.getSubtotal());
                itemResponses.add(itemResponse);
            }

            //  把订单项设置回订单响应对象中
            response.setItems(itemResponses);

            // 添加进最终结果列表
            result.add(response);
        }

        //  返回转换完成的订单列表
        return result;
    }


    // 商户接单：CREATED → ACCEPTED
    public void acceptOrder(UUID orderId, String restaurantId) {

        //  查询订单
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        // 校验是否属于当前餐厅
        validateOwnership(order, restaurantId);

        // 只有 CREATED 状态才能接单
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new RuntimeException("当前状态无法接单");
        }

        // 修改状态为 ACCEPTED
        order.setStatus(OrderStatus.ACCEPTED);

        // 保存到数据库
        orderRepository.save(order);
    }


    // 商户标记为发货
    public void markAsShipped(UUID orderId, String restaurantId) {

        // 根据 订单 ID 查找订单，找不到则抛异常
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        //  校验该订单是否属于当前商户（通过餐厅 ID 判断）
        validateOwnership(order, restaurantId);

        // 只有在订单状态是 ACCEPTED 时才允许发货
        if (order.getStatus() != OrderStatus.ACCEPTED) {
            throw new RuntimeException("当前状态无法发货，必须先接单");
        }

        // 修改订单状态为已发货
        order.setStatus(OrderStatus.SHIPPED);

        // 保存修改后的订单状态到数据库
        orderRepository.save(order);
    }

    // 商户取消订单
    public void cancelOrder(UUID orderId, String restaurantId) {

        // 根据 订单 ID 查找订单，找不到则抛异常
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        //  校验该订单是否属于当前商户（通过餐厅 ID 判断）
        validateOwnership(order, restaurantId);

        // 只有在订单状态是 CREATED 或 ACCEPTED 时才允许取消
        if (order.getStatus() != OrderStatus.CREATED && order.getStatus() != OrderStatus.ACCEPTED) {
            throw new RuntimeException("当前状态无法取消");
        }

        order.setStatus(OrderStatus.CANCELED);

        orderRepository.save(order);
    }

    // 校验订单是否属于当前餐厅
    private void validateOwnership(Order order, String restaurantId) {

        // 判断该订单的 restaurantId 是否和当前商户的餐厅 ID 相同
        if (!restaurantId.equals(order.getRestaurantId())) {
            throw new RuntimeException("无权限操作该订单");
        }
    }

}
