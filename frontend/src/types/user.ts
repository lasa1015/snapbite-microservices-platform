// src/types/user.ts


// 前端本地状态模型定义


export type Role = "USER" | "MERCHANT" | null;

export interface UserState {
  username: string | null;
  role: Role;
  setUsername: (name: string | null) => void;
  setRole: (role: Role) => void;
  fetchUser: () => Promise<void>;
}
