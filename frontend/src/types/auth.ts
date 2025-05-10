export type Role = "USER" | "MERCHANT"; // ✅ 新增类型定义

export type LoginResponse = {
  token: string;
};

export type RegisterRequest = {
  username: string;
  password: string;
  email: string;
  role: Role; // ✅ 使用 Role 类型
};

export type UserProfile = {
  username: string;
  role: Role;
};
