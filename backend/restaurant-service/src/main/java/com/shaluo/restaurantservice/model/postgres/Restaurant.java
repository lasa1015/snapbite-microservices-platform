package com.shaluo.restaurantservice.model.postgres;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "restaurants")
public class Restaurant {

    //  @JsonProperty(access = JsonProperty.Access.READ_ONLY): 这个字段只能被序列化成 JSON（即“读出来”给前端看），不能被反序列化（即“从前端发来的 JSON 填进去”）
    //  后端 → 前端（输出）时：id 会出现在 JSON 中
    //  前端 → 后端（输入）时：即使你在 JSON 里写了 "id": 100，后端也会忽略它，不会把 id 设置成 100
    //  对于主键 id 字段非常建议加，防止前端乱传 id
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
    /*在 restaurants 表中加 owner_id 字段，它是外键，实际存的是 User 的 id，但在 Java 对象里，我直接让你拿到 User 实体，方便查找。 */
    /* JPA 虽然在 Java 类中写的是整个对象 User，但在数据库中只会存该对象的主键 id 作为外键值，这是 JPA 自动做的映射机制。*/
    /* JPA 自动提取了 user.getId()，把它作为外键值存入 owner_id 字段中*/


    //  拆成微服务了，不要在数据库层面使用跨服务的外键（Foreign Key）引用
    //  微服务的推荐替代方案是：用 ID 引用 + API 协作
    @Column(name = "owner_id", nullable = false,unique = true)
    private Long ownerId;
}
