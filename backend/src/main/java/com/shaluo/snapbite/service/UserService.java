package com.shaluo.snapbite.service;

import com.shaluo.snapbite.dto.RegisterRequest;
import com.shaluo.snapbite.model.Role;
import com.shaluo.snapbite.model.User;
import com.shaluo.snapbite.repository.UserRepository;
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

    // 用户注册
    public User register(RegisterRequest request) {

        // 检查用户名是否已存在，避免重复注册
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在，确保唯一性
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }

        // DTO 转实体类
        // 创建空的实体对象
        User user = new User();

        //  设置用户名
        user.setUsername(request.getUsername());

        // 从request提取出密码，并使用passwordEncoder加密
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);

        // 设置邮箱
        user.setEmail(request.getEmail());

        try {
            // 将字符串角色（如 "user" / "merchant"）转换为枚举类型 Role
            // Role.valueOf(...) 是一个 Java 枚举类型（enum）的内置静态方法，用于将字符串转换成对应的枚举常量。
            Role role = Role.valueOf(request.getRole().toUpperCase());
            user.setRole(role);
        }

        // 若角色名称无法转换为枚举，抛出异常提示前端
        catch (IllegalArgumentException e) {
            throw new RuntimeException("无效的用户角色: " + request.getRole());
        }

        // 保存用户实体到数据库，返回保存后的 User 对象（包含主键 id）
        return userRepository.save(user);
    }

    // 用户登录
    public String login(String username, String password) {

        // 查询用户
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 比较密码（明文 vs 加密后）
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 登录成功，生成 JWT Token
        return jwtUtil.generateToken(user.getUsername(), user.getRole().name());
    }

}
