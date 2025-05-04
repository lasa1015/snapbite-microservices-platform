// OrderRepository.java
package com.shaluo.snapbite.repository;

import com.shaluo.snapbite.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}