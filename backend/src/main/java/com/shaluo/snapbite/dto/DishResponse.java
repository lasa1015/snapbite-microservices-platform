package com.shaluo.snapbite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DishResponse {

    private String id;
    private String name;
    private double price;

}
