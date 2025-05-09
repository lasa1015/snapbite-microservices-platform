package com.shaluo.userservice.controller;

import com.shaluo.userservice.dto.request.LoginRequest;
import com.shaluo.userservice.dto.request.RegisterRequest;
import com.shaluo.userservice.dto.response.UserResponse;
import com.shaluo.userservice.model.postgres.User;
import com.shaluo.userservice.repository.postgres.UserRepository;
import com.shaluo.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController  // 所有返回给前端的对象（包括 Map、实体类、DTO）都会自动变成 JSON，除非你自己干预（比如返回视图、文件等）。
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // 【注册接口】
    @PostMapping("/register")  // HTTP POST 请求接口，路径是 /register, 传的是 JSON 字符串
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request)  //请求体中的JSON数据要自动转换为Java对象RegisterRequest
    {

        // 调用 userService.register(...) 来真正执行注册逻辑
        userService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");

    }



    // 【登录接口】
    // Map<String, String> ：用户登录成功后，后端会生成一个 JWT Token 并返回给前端
    // Map<String, String> 会被 Spring Boot 的 Jackson 自动序列化为 JSON 对象返回给前端
    // 前端最终拿到的是一个 JSON 对象，格式类似{ "token": "eyJhbGciOiJIUzI1NiIsInR5..." }
    @PostMapping("/login")   // HTTP POST 请求接口
    public Map<String, String> login(
            @RequestBody LoginRequest request)  //把前端发来的 JSON 请求体，转换成 Java 对象 LoginRequest
    {

        // 调用 UserService 的 login 方法，传入用户名和密码，返回生成的 token
        String token = userService.login(request.getUsername(), request.getPassword());

        // 构造响应体，将 token 包装进一个 Map 对象中返回
        Map<String, String> response = new HashMap<>();

        response.put("token", token);

        // 返回前端 JSON 格式：{ "token": "eyJhbGciOiJIUzI1NiIsInR5..." }
        return response;
    }


    // 【获取当前登录用户信息 接口】
    // 当前端携带 JWT Token 访问 /me 接口时，后端会从请求头中解析 token，识别出用户身份，并返回该用户的完整信息（不含密码）。
    @GetMapping("/me")   //  HTTP GET 请求
    public UserResponse getCurrentUser() {

        // 从 Spring Security 上下文中获取当前认证对象
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 提取用户名（token中保存的 subject）
        String username = auth.getName();

        // 获取用户信息
        return userService.getUserProfile(username);

    }


    //【 供其他服务通过 username 获取 userId(例如 order-service，restaurant-service)】
    @GetMapping("/id/{username}")
    public Long getUserId(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        return user.getId();
    }


}
