package com.shaluo.snapbite.repository.postgres;

import com.shaluo.snapbite.model.postgres.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import com.shaluo.snapbite.model.postgres.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByUserOrderByCreatedAtDesc(User user);

    // 在 OrderRepository 添加
    @Query("SELECT DISTINCT o FROM Order o JOIN o.items i WHERE i.restaurantId = :restaurantId")
    List<Order> findOrdersByRestaurantId(@Param("restaurantId") String restaurantId);

}
