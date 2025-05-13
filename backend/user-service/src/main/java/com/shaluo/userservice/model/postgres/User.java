package com.shaluo.userservice.model.postgres;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

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

    // 【 User 和 Restaurant 是一对一关系，外键字段放在 Restaurant 表中，字段名是 owner。 】
    // 双向关联 结构 如果没有做 JSON 控制，就会导致：
    // 序列化时一直往返展开 owner → restaurant → owner → restaurant → … 无限循环！
    // @JsonIgnore 是 Jackson 提供的注解，在把 Java 对象转换成 JSON 的时候，忽略这个字段，不把它序列化到前端去。
//    @JsonIgnore
//    @OneToOne(mappedBy = "owner")  /* 外键在 Restaurant 表里存着的，字段名叫 owner 。user表没有列！*/
//    private Restaurant ownedRestaurant;


}
