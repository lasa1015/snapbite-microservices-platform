package com.shaluo.userservice.dto;

import lombok.Data;

// 注册请求 DTO。
// DTO 的作用就是“只接受你允许前端传的字段”
// 用于接收用户注册时提交的用户名、密码、邮箱和角色信息。
@Data
public class RegisterRequest {

    private String username;

    private String password;

    private String email;

    private String role;
}
