package com.shaluo.orderservice.service;

import com.shaluo.orderservice.client.CartClient;
import com.shaluo.orderservice.client.UserClient;
import com.shaluo.orderservice.dto.external.CartItemResponse;
import com.shaluo.orderservice.dto.external.RestaurantCartGroup;
import com.shaluo.orderservice.dto.request.CheckoutRequest;
import com.shaluo.orderservice.model.postgres.*;
import com.shaluo.orderservice.repository.postgres.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderUserServiceTest {

    private OrderRepository orderRepository;
    private CartClient cartClient;
    private UserClient userClient;
    private RabbitTemplate rabbitTemplate;
    private OrderUserService orderUserService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        cartClient = mock(CartClient.class);
        userClient = mock(UserClient.class);
        rabbitTemplate = mock(RabbitTemplate.class);

        orderUserService = new OrderUserService(
                orderRepository,
                cartClient,
                userClient,
                rabbitTemplate
        );
    }

    @Test
    void testCheckout_success() {
        String username = "john";

        // mock 请求体
        CheckoutRequest req = new CheckoutRequest();
        req.setRecipient("Alice");
        req.setPhone("1234567890");
        req.setAddress("123 Street");

        // mock 用户id
        when(userClient.getUserId(username)).thenReturn(101L);

        // mock 购物车数据
        CartItemResponse item = new CartItemResponse();
        item.setDishId("D1");
        item.setDishName("Pizza");
        item.setDishPrice(10.0);
        item.setQuantity(2);

        RestaurantCartGroup group = new RestaurantCartGroup("R1", "Pizza Place", List.of(item));
        when(cartClient.getGroupedCart(username)).thenReturn(List.of(group));

        orderUserService.checkout(username, req);

        // 验证订单保存 & cart 清空 & RabbitMQ 发消息
        verify(orderRepository, times(1)).save(any());
        verify(cartClient, times(1)).clearCart(username);
        verify(rabbitTemplate, times(1)).convertAndSend(
                eq("order.exchange"),
                eq("order.created"),
                any(Object.class) // ✅ 这里显式声明是 Object 类型，避免歧义
        );
    }

    @Test
    void testGetUserOrders_shouldReturnOrders() {
        String username = "john";
        when(userClient.getUserId(username)).thenReturn(202L);

        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setUserId(202L);
        order.setAddress("A1");
        order.setRecipient("R1");
        order.setPhone("111");
        order.setStatus(OrderStatus.CREATED);
        order.setTotalPrice(20.0);
        order.setCreatedAt(java.time.LocalDateTime.now());
        order.setRestaurantId("RID");
        order.setRestaurantName("RNAME");

        OrderItem item = new OrderItem();
        item.setDishId("D1");
        item.setDishName("Pizza");
        item.setDishPrice(10.0);
        item.setQuantity(2);
        item.setSubtotal(20.0);
        item.setRestaurantId("RID");
        order.setItems(List.of(item));

        when(orderRepository.findByUserIdOrderByCreatedAtDesc(202L)).thenReturn(List.of(order));

        var result = orderUserService.getUserOrders(username);
        assertEquals(1, result.size());
        assertEquals("Pizza", result.get(0).getItems().get(0).getDishName());
    }

    @Test
    void testCancelOrder_shouldSucceed() {
        String username = "john";
        UUID orderId = UUID.randomUUID();
        when(userClient.getUserId(username)).thenReturn(999L);

        Order order = new Order();
        order.setId(orderId);
        order.setUserId(999L);
        order.setStatus(OrderStatus.CREATED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderUserService.cancelOrder(username, orderId);

        assertEquals(OrderStatus.CANCELED, order.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testCancelOrder_wrongUser_shouldFail() {
        UUID orderId = UUID.randomUUID();
        when(userClient.getUserId("john")).thenReturn(1L);

        Order order = new Order();
        order.setUserId(999L); // 不同用户

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> orderUserService.cancelOrder("john", orderId));
        assertEquals("无权限取消该订单", ex.getMessage());
    }

    @Test
    void testConfirmOrder_success() {
        UUID orderId = UUID.randomUUID();
        when(userClient.getUserId("john")).thenReturn(200L);

        Order order = new Order();
        order.setUserId(200L);
        order.setStatus(OrderStatus.SHIPPED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderUserService.confirmOrder("john", orderId);
        assertEquals(OrderStatus.COMPLETED, order.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void testConfirmOrder_wrongStatus_shouldFail() {
        UUID orderId = UUID.randomUUID();
        when(userClient.getUserId("john")).thenReturn(200L);

        Order order = new Order();
        order.setUserId(200L);
        order.setStatus(OrderStatus.CREATED); // ❌不符合确认条件

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> orderUserService.confirmOrder("john", orderId));
        assertEquals("当前状态无法确认收货", ex.getMessage());
    }
}
