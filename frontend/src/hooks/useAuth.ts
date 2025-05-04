// src/hooks/useAuth.ts
import axios from "axios";

// 自定义 Hook，用于封装登录和注册逻辑
export function useAuth() {
  

  // 注册函数：传入完整的用户注册信息
  const register = async (data: {
    username: string;
    password: string;
    email: string;
    role: string;
  }) => {
    // 向后端发送注册请求
    await axios.post("/api/users/register", data);
  };

  

  // 登录函数：传入用户名和密码
  const login = async (username: string, password: string) => {

    // 发送登录请求，获取 token
    const loginRes = await axios.post("/api/users/login", { username, password });
    const token = loginRes.data.token;

    // 将 token 存入本地存储（localStorage）
    localStorage.setItem("token", token);

    // 用 token 请求当前登录用户的信息（即 /me 接口）
    const meRes = await axios.get("/api/users/me", {
      headers: { Authorization: `Bearer ${token}` }, // 加入认证头
    });

    // 拿到用户信息（用户名、角色等）
    const user = meRes.data;

    // 将用户名也存在本地（可选）
    localStorage.setItem("username", user.username);

    // 返回用户信息，给调用方使用
    return user;
  };





  // 将两个方法暴露出去
  return { login, register };
}
