package com.shaluo.snapbite.model.mongo;

import lombok.Data;

@Data
public class Dish {
    private Long id;
    private String name;
    private String description;
    private Double price;
}