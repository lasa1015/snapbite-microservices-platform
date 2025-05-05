import { createContext, useContext, useEffect, useState, ReactNode } from 'react';
import axios from 'axios';

type Role = "USER" | "MERCHANT" | null;

type UserContextType = {
  username: string | null;
  role: Role;
  setUsername: (name: string | null) => void;
  setRole: (role: Role) => void;
};

// 创建用户上下文，初始为 undefined
const UserContext = createContext<UserContextType | undefined>(undefined);

export function UserProvider({ children }: { children: ReactNode }) {
  const [username, setUsername] = useState<string | null>(null);
  const [role, setRole] = useState<Role>(null);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      axios.get("/api/users/me", {
        headers: {
          Authorization: `Bearer ${token}`
        }
      })
        .then(res => {
          setUsername(res.data.username);
          setRole(res.data.role); // ✅ 记录角色
        })
        .catch(() => {
          localStorage.removeItem("token");
          setUsername(null);
          setRole(null);
        });
    }
  }, []);

  return (
    <UserContext.Provider value={{ username, role, setUsername, setRole }}>
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
