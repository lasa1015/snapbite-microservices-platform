package com.shaluo.restaurantservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

//  主要是嵌在 MenuResponse 中被返回
@Data
@AllArgsConstructor
public class DishResponse {

    private Long id;
    private String name;
    private double dishPrice;
    private String description;

}