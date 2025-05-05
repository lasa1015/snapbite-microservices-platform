// hooks/useCartData.ts
import { useEffect, useState } from "react";

// 用于 localStorage 中游客购物车的 key
const LOCAL_KEY = "guest_cart";

// 统一定义购物车项的数据结构
export type CartItem = {
  id: string;
  restaurantId: string;
  restaurantName?: string;
  dishId: string;
  dishName?: string;
  price?: number;
  quantity: number;
};

// 核心 Hook：根据登录状态，读取购物车并提供操作函数
export function useCartData(reloadFlag: number) {
  const [cart, setCart] = useState<CartItem[]>([]);
  const [loading, setLoading] = useState(false);
  const token = localStorage.getItem("token");

  // 保存本地购物车并同步到组件状态
  const saveLocalCart = (items: CartItem[]) => {
    localStorage.setItem(LOCAL_KEY, JSON.stringify(items));
    setCart(items);
  };

  // 主函数：读取购物车（登录用户请求接口，游客读取 localStorage）
  const fetchCart = () => {
    setLoading(true);

    if (token) {

      // 登录用户：请求后端接口获取购物车
      fetch("/api/cart", {
        headers: { Authorization: `Bearer ${token}` },
      })
        .then((r) => r.json())
        .then(setCart)
        .catch(() => alert("🛒 获取购物车失败"))
        .finally(() => setLoading(false));
    } 
    
    else {
     
      // 未登录用户：读取本地 localStorage
      try {
        const local = localStorage.getItem(LOCAL_KEY);
        setCart(local ? JSON.parse(local) : []);
      } catch {
        setCart([]);
      } finally {
        setLoading(false);
      }
    }
  };

  // 监听 reloadFlag 变化时重新拉取购物车（外部触发刷新）
  useEffect(fetchCart, [reloadFlag]);

  // 更新某项数量
  const updateQuantity = async (id: string, q: number) => {
    if (q < 1) return;

    if (token) {
      
      // 登录用户：调用后端接口修改
      await fetch(`/api/cart/${id}/quantity?quantity=${q}`, { method: "PUT" });
      fetchCart();
    } 
    
    else {
      
      // 游客：更新 localStorage 中的记录
      const newCart = cart.map(item =>
        item.id === id ? { ...item, quantity: q } : item
      );
      saveLocalCart(newCart);
    }
  };

  // 删除某项
  const deleteItem = async (id: string) => {
    if (token) {
      await fetch(`/api/cart/${id}`, { method: "DELETE" });
      fetchCart();
    } else {
      const newCart = cart.filter(item => item.id !== id);
      saveLocalCart(newCart);
    }
  };

  // 清空购物车
  const clearCart = async () => {
    if (token) {
      await fetch("/api/cart/clear", {
        method: "DELETE",
        headers: { Authorization: `Bearer ${token}` },
      });
      fetchCart();
    } else {
      saveLocalCart([]);
    }
  };

  // 导出统一操作接口
  return {
    cart,           // 当前购物车内容
    loading,        // 是否正在加载中
    updateQuantity, // 更新数量方法
    deleteItem,     // 删除项方法
    clearCart       // 清空购物车方法
  };
}
