// src/stores/userStore.ts

// 管理当前用户信息（用户名、角色）的 Zustand 全局状态。
// 包含获取用户信息、设置用户名和角色的方法。

import { create } from "zustand";
import axios from "axios";
import { UserState } from "../types/user"; 




//  创建 Zustand 的用户状态管理 store
export const useUserStore = create<UserState>((set) => ({

  // 当前登录用户的用户名，未登录时为 null
  username: null,

  // 当前登录用户的角色：可能是 USER、MERCHANT 或 null
  role: null,

  // 设置用户名（登录成功后、或登出时调用）
  setUsername: (name) => set({ username: name }),

  // 设置角色（登录成功后、或登出时调用）
  setRole: (role) => set({ role }),



  // 尝试从后端获取当前用户信息（通常在刷新页面时调用）
  fetchUser: async () => {

    // 从本地存储中获取 JWT token
    const token = localStorage.getItem("token");

    if (!token) return; 

    try {

      // 使用token，请求用户信息的接口
      const res = await axios.get("/api/users/me", {

        headers: {
          Authorization: `Bearer ${token}`, // 设置 JWT 到请求头
        },
      });

      // 成功获取后更新 Zustand 状态
      set({
        username: res.data.username,
        role: res.data.role,
      });
    } 
    
    catch {
      // 如果 token 无效，清除本地状态和 token
      localStorage.removeItem("token");
      set({ username: null, role: null });
    }
  },
}));
