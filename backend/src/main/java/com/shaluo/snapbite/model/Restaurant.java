package com.shaluo.snapbite.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "restaurants")
public class Restaurant {

    //  @JsonProperty(access = JsonProperty.Access.READ_ONLY): 这个字段只能被序列化成 JSON（即“读出来”给前端看），不能被反序列化（即“从前端发来的 JSON 填进去”）
    //  后端 → 前端（输出）时：id 会出现在 JSON 中, 前端 → 后端（输入）时：即使你在 JSON 里写了 "id": 100，后端也会忽略它，不会把 id 设置成 100
    // 对于主键 id 字段非常建议加，防止前端乱传 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String name;
    private String yelpUrl;
    private String imgUrl;
    private String category;
    private Double rating;
    private String price;
    private Double latitude;
    private Double longitude;
    private String phone;
    private String address;
    private String mondayHours;
    private String tuesdayHours;
    private String wednesdayHours;
    private String thursdayHours;
    private String fridayHours;
    private String saturdayHours;
    private String sundayHours;
    private Boolean goodForBreakfast;
    private Boolean goodForBrunch;
    private Boolean goodForLunch;
    private Boolean goodForDinner;
    private String description;

    // 【 1个饭店，可以被一个user拥有 】
    @OneToOne
    @JoinColumn(name = "owner_id", nullable = false,unique = true) /* 指定数据库中这一列名为 owner_id，作为外键 */
    private User owner;
}
