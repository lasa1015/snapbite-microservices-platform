package com.shaluo.snapbite.service;

import com.shaluo.snapbite.dto.OrderItemDto;
import com.shaluo.snapbite.model.*;
import com.shaluo.snapbite.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Long userId, Long restaurantId, String address, List<OrderItemDto> itemDtos, double totalPrice) {
        Order order = new Order();
        order.setUserId(userId);
        order.setRestaurantId(restaurantId);
        order.setStatus(OrderStatus.CREATED);
        order.setAddress(address);
        order.setTotalPrice(totalPrice);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        List<OrderItem> items = itemDtos.stream().map(dto -> {
            OrderItem item = new OrderItem();
            item.setDishId(dto.getDishId());
            item.setName(dto.getName());
            item.setPrice(dto.getPrice());
            item.setQuantity(dto.getQuantity());
            item.setOrder(order);
            return item;
        }).toList();

        order.setItems(items);
        return orderRepository.save(order);
    }
}
