import { createContext, useContext, useEffect, useState, ReactNode } from 'react';
import axios from 'axios';

type UserContextType = {
  username: string | null;
  setUsername: (name: string | null) => void;
};

// 创建用户上下文，初始为 undefined
const UserContext = createContext<UserContextType | undefined>(undefined);

export function UserProvider({ children }: { children: ReactNode }) {
  const [username, setUsername] = useState<string | null>(null);

  
  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {

      // 使用 axios 请求当前登录用户的信息
      axios.get("/api/users/me", {
        headers: {
          Authorization: `Bearer ${token}`
        }
      })
        .then(res => {
          // 成功获取用户信息，设置用户名
          setUsername(res.data.username);
        })
        .catch(() => {
          // token 无效或请求失败，清除本地 token 并清空用户名
          localStorage.removeItem("token");
          setUsername(null);
        });
    }
  }, []);

  return (
    <UserContext.Provider value={{ username, setUsername }}>
      {children}
    </UserContext.Provider>
  );
}

// 自定义 hook：获取上下文值，保证只能在 UserProvider 中使用
export function useUser() {
  const context = useContext(UserContext);
  if (!context) {
    throw new Error("useUser must be used within a UserProvider");
  }
  return context;
}
