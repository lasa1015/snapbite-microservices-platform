package com.shaluo.userservice.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

// 用户信息响应 DTO：只返回安全字段，没有密码！
@Data
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String role;
    private LocalDateTime createdAt;
}
