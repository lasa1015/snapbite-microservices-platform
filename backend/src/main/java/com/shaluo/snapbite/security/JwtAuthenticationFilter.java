package com.shaluo.snapbite.security;

import com.shaluo.snapbite.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT 认证过滤器，用于拦截每一个请求：
 * - 从请求头中提取并校验 JWT Token
 * - 如果合法，则将认证信息存入 Spring Security 的上下文中
 * - 如果非法，则不设置上下文，Spring Security 会阻止访问受保护资源
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // 注入 JWT 工具类，用于解析和验证 token
    @Autowired
    private JwtUtil jwtUtil;

    // 每次请求都会经过这个方法
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 从请求头中获取名为 "Authorization" 的字段
        String authHeader = request.getHeader("Authorization");

        // 检查是否以 "Bearer " 开头（符合 JWT 的规范）
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            // 去掉前缀，提取实际的 JWT 字符串
            String token = authHeader.substring(7);

            try {

                // 调用工具类解析 token，返回 Claims（包含用户名、角色等）
                Claims claims = jwtUtil.parseToken(token);
                String username = claims.getSubject(); // subject 通常是用户名
                String role = claims.get("role", String.class); // 自定义字段：角色

                // 构建一个 Spring Security 的认证对象（这里只写用户名，权限留空）
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());

                // 给这个认证对象附加请求信息（如 IP、浏览器等）
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 将这个认证对象存入 Spring 的上下文，后续可以识别为已登录用户
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            catch (Exception e) {
                // 如果 token 不合法或已过期，就什么也不做（不设置认证上下文）
                System.out.println("JWT Token 无效或已过期: " + e.getMessage());
            }
        }

        // 无论 token 是否通过校验，继续执行后续过滤器链（但权限控制在后面）
        filterChain.doFilter(request, response);
    }
}
