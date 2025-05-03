package com.shaluo.snapbite.config;

import com.shaluo.snapbite.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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

        /* ---- 白名单路径 ---- */
        String[] PUBLIC_API = {
                "/api/users/register",
                "/api/users/login",
                "/api/restaurants/**",
                "/api/menu/restaurant/**",
                "/api/menu/dishes",
                "/api/cart/**"
        };

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_API).permitAll()
                        .anyRequest().authenticated()
                )
                /* 自定义过滤逻辑：白名单路径不做 JWT 校验 */
                .addFilterBefore((request, response, chain) -> {
                    HttpServletRequest req = (HttpServletRequest) request;
                    for (String pattern : PUBLIC_API) {
                        if (new AntPathRequestMatcher(pattern).matches(req)) {
                            chain.doFilter(request, response);   // 直接放行
                            return;
                        }
                    }
                    // 其余请求再交给 JWT 过滤器
                    jwtAuthenticationFilter.doFilter(request, response, chain);
                }, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }
}
