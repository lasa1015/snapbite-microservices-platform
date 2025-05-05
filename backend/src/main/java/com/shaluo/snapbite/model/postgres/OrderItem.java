package com.shaluo.snapbite.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Order order;

    private String restaurantId;

    private String dishId;
    private String dishName;
    private Double price;
    private Integer quantity;
}
