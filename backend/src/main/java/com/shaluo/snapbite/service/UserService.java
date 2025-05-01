package com.shaluo.snapbite.service;

import com.shaluo.snapbite.dto.RegisterRequest;
import com.shaluo.snapbite.model.Role;
import com.shaluo.snapbite.model.User;
import com.shaluo.snapbite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // ❗实际开发中应加密存储
        user.setEmail(request.getEmail());

        try {
            // 将字符串角色名转换为枚举值（不区分大小写）
            Role role = Role.valueOf(request.getRole().toUpperCase());
            user.setRole(role);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("无效的用户角色: " + request.getRole());
        }

        return userRepository.save(user);
    }
}
