package com.shaluo.snapbite.model.mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class Dish {

    @Field("id")
    private Long id;
    private String name;
    private String description;
    private Double price;
}