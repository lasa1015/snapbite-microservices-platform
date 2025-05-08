package com.shaluo.restaurantservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

//  主要是嵌在 MenuResponse 中被返回
@Data
@AllArgsConstructor
public class DishResponse {

    private String id;
    private String name;
    private double price;
    private String description;

}