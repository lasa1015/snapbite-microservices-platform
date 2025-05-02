package com.shaluo.snapbite.controller;

import com.shaluo.snapbite.dto.LoginRequest;
import com.shaluo.snapbite.dto.RegisterRequest;
import com.shaluo.snapbite.model.User;
import com.shaluo.snapbite.repository.UserRepository;
import com.shaluo.snapbite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

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



    // 需要登录才能访问的接口。返回当前登录用户信息
    @GetMapping("/me")
    public User getCurrentUser() {

        // 从 Spring Security 上下文中获取当前认证对象
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 提取用户名（token中保存的 subject）
        String username = auth.getName();

        // 根据用户名查找完整用户信息（不含密码）
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }



}
