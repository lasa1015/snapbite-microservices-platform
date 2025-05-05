// src/stores/userStore.ts
import { create } from "zustand";
import axios from "axios";
import { Role } from "../types/user";

interface UserState {
  username: string | null;
  role: Role;
  setUsername: (name: string | null) => void;
  setRole: (role: Role) => void;
  fetchUser: () => Promise<void>;
}

export const useUserStore = create<UserState>((set) => ({
  username: null,
  role: null,
  setUsername: (name) => set({ username: name }),
  setRole: (role) => set({ role }),
  fetchUser: async () => {
    const token = localStorage.getItem("token");
    if (!token) return;

    try {
      const res = await axios.get("/api/users/me", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      set({ username: res.data.username, role: res.data.role });
    } catch {
      localStorage.removeItem("token");
      set({ username: null, role: null });
    }
  },
}));
