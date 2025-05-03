package com.shaluo.snapbite.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

// 生成和验证 Token
@Component
public class JwtUtil {

    // 只有拥有这个密钥的服务才能生成或验证 token。别人伪造不了。
    // SECRET_KEY（签名密钥）是 JWT 加密和验证的核心，务必保证机密。
    // @Value("${JWT_SECRET}") 从 Spring 的配置环境中读取
    @Value("${JWT_SECRET}")
    private String secret;

    // Token 过期时间（单位：毫秒）例如 24 小时
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    // 生成jwt token
    public String generateToken(String username, String role) {

        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        // 通过 Jwts.builder() 构造 JWT 格式内容
        return Jwts.builder()

                // 设置用户身份信息（JWT 的 subject）
                .setSubject(username)

                // 自定义字段，设置用户角色（会保存在 Payload 中）
                .claim("role", role)

                // 签发时间（iat）
                .setIssuedAt(new Date())

                // 设置过期时间（exp），比如现在时间 + 24小时
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))

                // 用 HMAC SHA256 算法签名，并附上密钥，确保不会被伪造
                .signWith(key, SignatureAlgorithm.HS256)

                // 构造并返回 JWT 字符串
                .compact();
    }


    // 解析 JWT 并返回用户信息（username、role 等）
    public Claims parseToken(String token) {

        // 生成用于验证签名的密钥（要与生成 token 时的密钥保持一致）
        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        // 构建 JWT 解析器，设置签名密钥后解析并验证 token
        return Jwts.parserBuilder()
                .setSigningKey(key) // 设置用于校验签名的密钥
                .build()
                .parseClaimsJws(token) // 解析并验证 token（签名 + 时间等）
                .getBody(); // 如果成功，提取出 Payload 部分（就是 Claims）
    }

}
