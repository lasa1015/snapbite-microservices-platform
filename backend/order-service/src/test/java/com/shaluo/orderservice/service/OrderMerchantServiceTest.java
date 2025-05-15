package com.shaluo.orderservice.service;

import com.shaluo.orderservice.client.RestaurantClient;
import com.shaluo.orderservice.model.postgres.Order;
import com.shaluo.orderservice.model.postgres.OrderItem;
import com.shaluo.orderservice.model.postgres.OrderStatus;
import com.shaluo.orderservice.repository.postgres.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderMerchantServiceTest {

    private OrderRepository orderRepository;
    private RestaurantClient restaurantClient;
    private OrderMerchantService service;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        restaurantClient = mock(RestaurantClient.class);
        service = new OrderMerchantService(orderRepository, restaurantClient);
    }

    // 构造函数注入版本（你需要修改 OrderMerchantService 支持构造器注入）
    public OrderMerchantServiceTest() {}

    @Test
    void testGetMyRestaurantOrders_success() {
        String merchantUsername = "merchant1";
        String restId = "R123";

        Order order = createOrder(restId, OrderStatus.CREATED);

        when(restaurantClient.getRestaurantIdByOwnerUsername(merchantUsername)).thenReturn(restId);
        when(orderRepository.findOrdersByRestaurantId(restId)).thenReturn(List.of(order));

        var result = service.getMyRestaurantOrders(merchantUsername);
        assertEquals(1, result.size());
        assertEquals("R123", result.get(0).getRestaurantId());
    }

    @Test
    void testAcceptOrder_shouldSucceed() {
        UUID orderId = UUID.randomUUID();
        Order order = createOrder("R001", OrderStatus.CREATED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        service.acceptOrder(orderId, "R001");

        assertEquals(OrderStatus.ACCEPTED, order.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void testAcceptOrder_invalidStatus_shouldFail() {
        UUID orderId = UUID.randomUUID();
        Order order = createOrder("R001", OrderStatus.SHIPPED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        var ex = assertThrows(RuntimeException.class, () -> service.acceptOrder(orderId, "R001"));
        assertEquals("当前状态无法接单", ex.getMessage());
    }

    @Test
    void testMarkAsShipped_shouldSucceed() {
        UUID orderId = UUID.randomUUID();
        Order order = createOrder("R001", OrderStatus.ACCEPTED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        service.markAsShipped(orderId, "R001");

        assertEquals(OrderStatus.SHIPPED, order.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void testCancelOrder_successFromCreated() {
        UUID orderId = UUID.randomUUID();
        Order order = createOrder("R001", OrderStatus.CREATED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        service.cancelOrder(orderId, "R001");

        assertEquals(OrderStatus.CANCELED, order.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void testCancelOrder_invalidStatus_shouldFail() {
        UUID orderId = UUID.randomUUID();
        Order order = createOrder("R001", OrderStatus.SHIPPED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        var ex = assertThrows(RuntimeException.class, () -> service.cancelOrder(orderId, "R001"));
        assertEquals("当前状态无法取消", ex.getMessage());
    }

    @Test
    void testOwnershipCheck_shouldFailIfMismatched() {
        UUID orderId = UUID.randomUUID();
        Order order = createOrder("R999", OrderStatus.CREATED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.cancelOrder(orderId, "R001");
        });
        assertEquals("无权限操作该订单", ex.getMessage());
    }

    // 工具方法构造订单
    private Order createOrder(String restId, OrderStatus status) {
        Order o = new Order();
        o.setId(UUID.randomUUID());
        o.setRestaurantId(restId);
        o.setRestaurantName("Test Restaurant");
        o.setStatus(status);
        o.setCreatedAt(LocalDateTime.now());
        o.setItems(List.of(createOrderItem(restId)));
        return o;
    }

    private OrderItem createOrderItem(String restId) {
        OrderItem item = new OrderItem();
        item.setDishId("D1");
        item.setDishName("Pizza");
        item.setDishPrice(10.0);
        item.setQuantity(2);
        item.setSubtotal(20.0);
        item.setRestaurantId(restId);
        return item;
    }
}
