package com.shaluo.snapbite.repository.postgres;

import com.shaluo.snapbite.model.postgres.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import com.shaluo.snapbite.model.postgres.User;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByUserOrderByCreatedAtDesc(User user);
}
