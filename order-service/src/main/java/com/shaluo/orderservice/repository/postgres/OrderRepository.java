package com.shaluo.orderservice.repository.postgres;

import com.shaluo.orderservice.model.postgres.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId); // ✅ 保留

    @Query("SELECT DISTINCT o FROM Order o JOIN o.items i WHERE i.restaurantId = :restaurantId")
    List<Order> findOrdersByRestaurantId(@Param("restaurantId") String restaurantId); // ✅ 商户查询订单可用
}
