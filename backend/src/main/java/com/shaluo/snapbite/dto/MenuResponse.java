package com.shaluo.snapbite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MenuResponse {

    private Integer restaurantId;

    private List<DishResponse> dishes;
}
