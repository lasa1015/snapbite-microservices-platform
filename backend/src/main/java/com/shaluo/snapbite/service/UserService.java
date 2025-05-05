package com.shaluo.snapbite.service;

import com.shaluo.snapbite.dto.RegisterRequest;
import com.shaluo.snapbite.dto.UserResponse;
import com.shaluo.snapbite.model.postgres.Role;
import com.shaluo.snapbite.model.postgres.User;
import com.shaluo.snapbite.repository.postgres.UserRepository;
import com.shaluo.snapbite.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // 自动注入加密

    @Autowired
    private JwtUtil jwtUtil; // 注入jwt

    // 【用户注册】
    // 接受的参数是一个DTO
    public void register(RegisterRequest request) {

        // 检查用户名是否已存在，避免重复注册
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在，确保唯一性
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }

        // DTO 转实体类
        // 创建空的实体对象 User
        User user = new User();

        //  从DTO读取用户名并设置给实体
        user.setUsername(request.getUsername());

        // 从DTO提取出密码，并使用Spring Security 已经内置的 PasswordEncoder加密
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 给实体设置
        user.setPassword(encodedPassword);

        // 设置邮箱
        user.setEmail(request.getEmail());

        // 将字符串角色（如 "user" / "merchant"）转换为枚举类型 Role
        // Role.valueOf(...) 是一个 Java 枚举类型（enum）的内置静态方法，用于将字符串转换成对应的枚举常量。
        user.setRole(Role.valueOf(request.getRole()));

        // 保存user进数据库
        User saved = userRepository.save(user);
    }


    // 【用户登录】
    public String login(String username, String password) {

        // 查询找到用户
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 判断这个明文密码，经过加密后是否等于数据库里保存的加密版本
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 登录成功，生成 JWT Token
        return jwtUtil.generateToken(user.getUsername(), user.getRole().name());
    }


    // 【根据用户名，获得用户信息，并转为DTO返回】
    public UserResponse getUserProfile(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 手动转换为 UserResponse（DTO）
        UserResponse res = new UserResponse();
        res.setId(user.getId());
        res.setUsername(user.getUsername());
        res.setEmail(user.getEmail());
        res.setRole(user.getRole().name());
        res.setCreatedAt(user.getCreatedAt());

        return res;
    }


}
