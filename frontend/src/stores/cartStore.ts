import { create } from 'zustand';

// ✅ 定义 Store 类型
interface CartStore {
  // 原有部分：用于刷新购物车数据
  reloadFlag: number;
  triggerReload: () => void;

  // 新增部分：控制购物车 UI 显示
  cartOpen: boolean;
  openCart: () => void;
  closeCart: () => void;
}

// ✅ 创建 Zustand store
export const useCartStore = create<CartStore>((set) => ({
  // 原有逻辑
  reloadFlag: 0,
  triggerReload: () => set((state) => ({ reloadFlag: state.reloadFlag + 1 })),

  // 新增逻辑
  cartOpen: false,
  openCart: () => set({ cartOpen: true }),
  closeCart: () => set({ cartOpen: false }),
}));
