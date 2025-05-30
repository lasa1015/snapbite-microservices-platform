package com.shaluo.orderservice.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @ManyToOne
    private Order order;

    private String restaurantId;

    private String dishId;
    private String dishName;
    private Double dishPrice;
    private Integer quantity;

    @Column(nullable = true)
    private Double subtotal;

}
