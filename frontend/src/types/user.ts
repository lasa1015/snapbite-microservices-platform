// src/types/user.ts

export type Role = "USER" | "MERCHANT" | null;

export type UserContextType = {
  username: string | null;
  role: Role;
  setUsername: (name: string | null) => void;
  setRole: (role: Role) => void;
};
