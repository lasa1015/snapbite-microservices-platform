// src/store/cartStore.ts (使用 Zustand 替代 Context)

import { create } from 'zustand';

// ✅ 定义 Store 类型
interface CartStore {
  reloadFlag: number;
  triggerReload: () => void;
}

// ✅ 创建 Zustand store
export const useCartStore = create<CartStore>((set) => ({
  reloadFlag: 0,
  triggerReload: () => set((state) => ({ reloadFlag: state.reloadFlag + 1 }))
}));
