import { create } from 'zustand';
import { CartGroup } from '../types/cart'; // ✅ 正确


interface CartStore {
  reloadFlag: number;
  triggerReload: () => void;

  cartOpen: boolean;
  openCart: () => void;
  closeCart: () => void;


  cart: CartGroup[]; // 每组是一家餐厅的菜品
  setCartItems: (items: CartGroup[]) => void;
}

export const useCartStore = create<CartStore>((set) => ({
  reloadFlag: 0,
  triggerReload: () => set((state) => ({ reloadFlag: state.reloadFlag + 1 })),

  cartOpen: false,
  openCart: () => set({ cartOpen: true }),
  closeCart: () => set({ cartOpen: false }),

  cart: [], // ✅ 初始购物车为空
  setCartItems: (items) => set({ cart: items }), // ✅ 设置购物车分组数据
}));
