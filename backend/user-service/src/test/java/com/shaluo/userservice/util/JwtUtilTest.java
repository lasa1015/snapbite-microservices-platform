package com.shaluo.userservice.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        String fakeSecret = "this_is_a_very_secret_key_1234567890";
        jwtUtil = new JwtUtil(fakeSecret); // ✅ 直接构造注入
    }

    @Test
    void generateToken_shouldContainCorrectUsernameAndRole() {
        String token = jwtUtil.generateToken("alice", "USER");
        assertNotNull(token);
        assertTrue(token.length() > 10);
    }

    @Test
    void parseToken_shouldReturnCorrectClaims() {
        String token = jwtUtil.generateToken("bob", "ADMIN");
        Claims claims = jwtUtil.parseToken(token);
        assertEquals("bob", claims.getSubject());
        assertEquals("ADMIN", claims.get("role"));
    }
}

