package com.shaluo.userservice.security;

import com.shaluo.userservice.util.JwtUtil;
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

// OncePerRequestFilter 是 Spring 提供的一个抽象类，保证每个请求只会经过一次这个过滤器。
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // 自己写的工具类，专门用来“解析和验证 token”
    @Autowired
    private JwtUtil jwtUtil;

    // 每次请求都会经过这个方法
    // doFilter() 方法里面包含了doFilterInternal()的调用
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 从请求头中获取名为 "Authorization" 的字段
        String authHeader = request.getHeader("Authorization");

        // 检查请求头是不是null，是否以 "Bearer " 开头（符合 JWT 的规范）
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            //  去掉 "Bearer " 这 7 个字符，拿到真正的 Token 内容
            String token = authHeader.substring(7);

            try {

                // 用工具类 jwtUtil 去解析 token，验证token是否合法
                // 如果合法会返回claims对象
                Claims claims = jwtUtil.parseToken(token);

                // claims.getSubject() 是你之前在登录时设置的用户名
                String username = claims.getSubject();

                // claims.get("role") 是你设置的角色（如果有）
                String role = claims.get("role", String.class);

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
