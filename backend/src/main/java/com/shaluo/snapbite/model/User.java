package com.shaluo.snapbite.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password; // 后续使用加密存储

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // 【 1：1 一个用户，管理1个餐厅 】
    // 双向关联 结构 如果没有做 JSON 控制，就会导致：
    // 序列化时一直往返展开 owner → restaurant → owner → restaurant → … 无限循环！
    // @JsonIgnore 是 Jackson 提供的注解，在把 Java 对象转换成 JSON 的时候，忽略这个字段，不把它序列化到前端去。
    @JsonIgnore
    @OneToOne(mappedBy = "owner")  /* 外键在 Restaurant 表里存着的，字段名叫 owner */
    private Restaurant ownedRestaurant;


}
