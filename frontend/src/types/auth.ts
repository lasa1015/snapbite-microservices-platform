// src/types/auth.ts

// 登录、注册用的结构


// 登录响应：后端返回的登录结果，一般是 JWT token
export type LoginResponse = {
  token: string;  // 认证令牌
};


// 注册请求：前端发送给后端的注册数据结构
export type RegisterRequest = {
  username: string; 
  password: string; 
  email: string;    
  role: string;     // 用户角色（例如 "USER" 或 "MERCHANT"）
};


// 登录后获取的用户信息
export type UserProfile = {
  username: string;                 
  role: "USER" | "MERCHANT";        
};
