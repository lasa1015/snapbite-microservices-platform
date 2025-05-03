package com.shaluo.snapbite.dto;

import lombok.Data;

@Data
public class RestaurantResponse {
    private Long id;
    private String name;
    private String imgUrl;
    private String displayAddress;
    private Double rating;
    private Integer reviewCount;
    private String price;
    private String category;
    private String description;
}
