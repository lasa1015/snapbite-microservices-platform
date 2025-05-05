import axios from "axios";
import { RegisterRequest, UserProfile } from "../types/auth"; // ✅ 引入类型

// 自定义 Hook，用于封装登录和注册逻辑
export function useAuth() {

  // 注册函数：传入完整的用户注册信息
  const register = async (data: RegisterRequest) => {
    await axios.post("/api/users/register", data);
  };

  // 登录函数：传入用户名和密码
  const login = async (username: string, password: string): Promise<UserProfile> => {
    const loginRes = await axios.post("/api/users/login", { username, password });
    const token = loginRes.data.token;
    localStorage.setItem("token", token);

    const meRes = await axios.get("/api/users/me", {
      headers: { Authorization: `Bearer ${token}` },
    });

    const user = meRes.data;
    localStorage.setItem("username", user.username);

    return user;
  };

  return { login, register };
}
