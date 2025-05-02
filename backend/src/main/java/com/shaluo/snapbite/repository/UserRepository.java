package com.shaluo.snapbite.repository;

import com.shaluo.snapbite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 根据用户名查找用户对象。
    // 通常用于登录认证或加载用户信息。
    Optional<User> findByUsername(String username);

    // 检查某个用户名是否已存在。
    // 注册时常用来防止用户名重复。
    boolean existsByUsername(String username);

    // 检查某个邮箱是否已被注册。
    // 可用于邮箱唯一性校验。
    boolean existsByEmail(String email);
}
