package com.shaluo.snapbite.dto;

import lombok.Data;

// 返回给前端的
@Data
public class RestaurantResponse {
    private Long id;
    private String name;
    private String imgUrl;
    private String displayAddress;
    private Double rating;
    private String price;
    private String category;
    private String description;

    private Double latitude;
    private Double longitude;
}
