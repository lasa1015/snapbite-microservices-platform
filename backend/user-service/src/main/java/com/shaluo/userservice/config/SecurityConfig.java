package com.shaluo.userservice.config;

import com.shaluo.userservice.security.JwtAuthenticationFilter;
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

    // 核心安全配置
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 白名单, 这些路径不需要登录就能访问
        String[] PUBLIC_API = {
                "/api/users/register",
                "/api/users/login",
                "/api/users/id/**",

                // ✅ Swagger 相关路径（用于 API 文档界面和数据）
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/webjars/**" // 如果你用的是旧版 swagger-ui，可能需要这项

        };

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_API).permitAll()   // 白名单直接放行
                        .anyRequest().authenticated()        // 其他请求必须登录
                )

                // JWT 拦截器逻辑
                .addFilterBefore((request, response, chain) -> {

                    HttpServletRequest req = (HttpServletRequest) request;

                    // 检查请求是不是在白名单
                    for (String pattern : PUBLIC_API) {

                        //  如果是 → 放行，不做任何身份验证
                        if (new AntPathRequestMatcher(pattern, null).matches(req)) {
                            chain.doFilter(request, response);
                            return;
                        }
                    }

                    // 如果不是（例如 /api/users/me） → 进入自定义的 JWT 过滤器 JwtAuthenticationFilter (被注入进来)
                    // 调用过滤器中的方法 doFilterInternal()
                    // doFilter()里包含了doFilterInternal()，因此写doFilter会自动调用doFilterInternal
                    jwtAuthenticationFilter.doFilter(request, response, chain);
                }, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }
}
