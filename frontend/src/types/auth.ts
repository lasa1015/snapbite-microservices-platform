// src/types/auth.ts

export type RegisterRequest = {
  username: string;
  password: string;
  email: string;
  role: string;
};

export type LoginResponse = {
  token: string;
};

export type UserProfile = {
  username: string;
  role: "USER" | "MERCHANT";
};
