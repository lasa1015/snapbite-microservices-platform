package com.shaluo.snapbite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// 以后哪里需要 PasswordEncoder，就自动注入 BCryptPasswordEncoder
@Configuration
public class SecurityConfig {

    // 密码加密
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
                        .requestMatchers("/api/users/register",
                                "/api/users/login").permitAll()

                        // 其余接口必须认证
                        .anyRequest().authenticated())

                // 先保留 httpBasic，方便用 Postman 带账号测试；等 JWT 做好再移除
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
