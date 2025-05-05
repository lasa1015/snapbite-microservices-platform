import { useEffect, useState } from "react";
import { CartItem } from "../types/cart";
import {
  fetchCartFromServer,
  updateItemQuantity,
  deleteCartItem,
  clearCartOnServer,
} from "../services/cartService";

const LOCAL_KEY = "guest_cart";

export function useCartData(reloadFlag: number) {
  const [cart, setCart] = useState<CartItem[]>([]);
  const [loading, setLoading] = useState(false);
  const token = localStorage.getItem("token");

  const saveLocalCart = (items: CartItem[]) => {
    localStorage.setItem(LOCAL_KEY, JSON.stringify(items));
    setCart(items);
  };

  const fetchCart = async () => {
    setLoading(true);
    try {
      if (token) {
        const data = await fetchCartFromServer(token);
        setCart(data);
      } else {
        const local = localStorage.getItem(LOCAL_KEY);
        setCart(local ? JSON.parse(local) : []);
      }
    } catch {
      alert("🛒 获取购物车失败");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCart();
  }, [reloadFlag]);

  const updateQuantity = async (id: string, q: number) => {
    if (q < 1) return;
    if (token) {
      await updateItemQuantity(id, q, token);
      fetchCart();
    } else {
      const newCart = cart.map(item =>
        item.id === id ? { ...item, quantity: q } : item
      );
      saveLocalCart(newCart);
    }
  };

  const deleteItem = async (id: string) => {
    if (token) {
      await deleteCartItem(id, token);
      fetchCart();
    } else {
      saveLocalCart(cart.filter(item => item.id !== id));
    }
  };

  const clearCart = async () => {
    if (token) {
      await clearCartOnServer(token);
      fetchCart();
    } else {
      saveLocalCart([]);
    }
  };

  return { cart, loading, updateQuantity, deleteItem, clearCart };
}
