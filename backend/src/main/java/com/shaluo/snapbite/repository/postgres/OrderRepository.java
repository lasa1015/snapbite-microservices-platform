package com.shaluo.snapbite.repository.postgres;

import com.shaluo.snapbite.model.postgres.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUsernameOrderByCreatedAtDesc(String username);
}
