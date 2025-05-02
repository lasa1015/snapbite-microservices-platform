package com.shaluo.snapbite.config;

import com.shaluo.snapbite.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// 以后哪里需要 PasswordEncoder，就自动注入 BCryptPasswordEncoder
@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // 密码加密
    // BCrypt 是一种 哈希函数，专门用于加密密码。
    // 不可逆（不是加密而是哈希）,就算数据库被黑客拿到了密码哈希值，也无法通过计算还原出明文密码。
    // 它每次加密都会自动生成一个随机“盐值”，即使两次加密相同密码，结果也完全不同。这样可以有效防止“彩虹表攻击”。
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 基础的安全规则
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())          // 先关闭 CSRF（前后端分离常见做法）
                .authorizeHttpRequests(auth -> auth

                        // 放行的接口
                        .requestMatchers(
                                "/api/users/register",
                                "/api/users/login",
                                "/api/users/me",
                        "/api/restaurants/**"
                         )
                        .permitAll()

                        // 其余接口必须认证
                        .anyRequest().authenticated())

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
