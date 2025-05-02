package com.shaluo.snapbite.controller;

import com.shaluo.snapbite.dto.LoginRequest;
import com.shaluo.snapbite.dto.RegisterRequest;
import com.shaluo.snapbite.model.User;
import com.shaluo.snapbite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 注册接口
    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    // 登录接口
    // 用户登录成功后，后端会生成一个 JWT Token 并返回给前端
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {

        // 调用 UserService 的 login 方法，传入用户名和密码，返回生成的 token
        String token = userService.login(request.getUsername(), request.getPassword());

        // 构造响应体，将 token 包装进一个 Map 对象中返回
        Map<String, String> response = new HashMap<>();

        response.put("token", token);

        // 返回前端 JSON 格式：{ "token": "xxx.yyy.zzz" }
        return response;
    }


}
