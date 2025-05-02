package com.shaluo.snapbite.dto;

import lombok.Data;

// 登录请求 DTO：用于接收前端传来的登录数据
@Data
public class LoginRequest {
    private String username;
    private String password;
}
