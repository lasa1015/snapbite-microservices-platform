// src/types/user.ts


// 前端本地状态模型定义



export type Role = "USER" | "MERCHANT";

export interface UserState {
  username: string | null;
  role: Role | null;
  setUsername: (name: string | null) => void;
  setRole: (role: Role | null) => void;
  fetchUser: () => Promise<void>;
}
