//package com.shaluo.orderservice.service;
//
//import com.shaluo.orderservice.client.RestaurantClient;
//import com.shaluo.orderservice.dto.response.OrderItemResponse;
//import com.shaluo.orderservice.dto.response.OrderResponse;
//import com.shaluo.orderservice.model.postgres.Order;
//import com.shaluo.orderservice.model.postgres.OrderStatus;
//import com.shaluo.orderservice.repository.postgres.OrderRepository;
//import com.shaluo.snapbite.repository.postgres.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//
//@Service
//public class OrderMerchantService {
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private RestaurantClient restaurantClient;
//
//
//    public List<OrderResponse> getMyRestaurantOrders(String merchantUsername) {
//
//        // 封装 Feign 调用逻辑进 Service 层
//        String restaurantId = restaurantClient.getRestaurantIdByOwnerUsername(merchantUsername);
//
//        return getOrdersByRestaurant(restaurantId); // 复用已有方法
//    }
//
//
//    // 商家查看属于自己餐厅的所有订单项（按餐厅ID筛选）
//    public List<OrderResponse> getOrdersByRestaurant(String restaurantId) {
//        List<Order> orders = orderRepository.findOrdersByRestaurantId(restaurantId);
//
//        return orders.stream().map(order -> {
//            OrderResponse response = new OrderResponse();
//            response.setId(order.getId().toString());
//            response.setAddress(order.getAddress());
//            response.setRecipient(order.getRecipient());
//            response.setPhone(order.getPhone());
//            response.setStatus(order.getStatus().name());
//            response.setTotalPrice(order.getTotalPrice());
//            response.setCreatedAt(order.getCreatedAt());
//
//            List<OrderItemResponse> itemResponses = order.getItems().stream()
//                    .filter(item -> restaurantId.equals(item.getRestaurantId())) // 保证只返回当前商家的菜品
//                    .map(item -> {
//                        OrderItemResponse i = new OrderItemResponse();
//                        i.setDishName(item.getDishName());
//                        i.setPrice(item.getPrice());
//                        i.setQuantity(item.getQuantity());
//                        i.setRestaurantId(item.getRestaurantId());
//                        return i;
//                    }).toList();
//
//            response.setItems(itemResponses);
//            return response;
//        }).toList();
//    }
//
//
//    // 商家发货（更新订单状态为 SHIPPED）
//    public void markAsShipped(UUID orderId, String restaurantId) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("订单不存在"));
//
//        boolean hasRestaurantItem = order.getItems().stream()
//                .anyMatch(i -> restaurantId.equals(i.getRestaurantId()));
//
//        if (!hasRestaurantItem) {
//            throw new RuntimeException("该订单不属于当前商家");
//        }
//
//        if (order.getStatus() != OrderStatus.CREATED) {
//            throw new RuntimeException("当前状态无法发货");
//        }
//
//        order.setStatus(OrderStatus.SHIPPED);
//        orderRepository.save(order);
//    }
//
//    // 商家取消订单（更新订单状态为 CANCELED）
//    public void cancelOrder(UUID orderId, String restaurantId) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("订单不存在"));
//
//        boolean hasRestaurantItem = order.getItems().stream()
//                .anyMatch(i -> restaurantId.equals(i.getRestaurantId()));
//
//        if (!hasRestaurantItem) {
//            throw new RuntimeException("该订单不属于当前商家");
//        }
//
//        if (order.getStatus() != OrderStatus.CREATED) {
//            throw new RuntimeException("当前状态无法取消");
//        }
//
//        order.setStatus(OrderStatus.CANCELED);
//        orderRepository.save(order);
//    }
//}
