import { useEffect, useState } from "react";
import { CartItem } from "../types/cart";
import {
  fetchCartFromServer,
  updateItemQuantity,
  deleteCartItem,
  clearCartOnServer,
} from "../services/cartService";
import { useToast } from "../components/Toast";


const LOCAL_KEY = "guest_cart";

type CartGroup = {
  restaurantId: string;
  restaurantName: string;
  items: CartItem[];
};

export function useCartData(reloadFlag: number) {
  const [cart, setCart] = useState<CartGroup[]>([]);
  const [loading, setLoading] = useState(false);
  const token = localStorage.getItem("token");
  const showToast = useToast();

  const saveLocalCart = (groups: CartGroup[]) => {
    localStorage.setItem(LOCAL_KEY, JSON.stringify(groups));
    setCart(groups);
  };

  const fetchCart = async () => {
    setLoading(true);
    try {
      if (token) {
        const data = await fetchCartFromServer(token); // 应该返回的是 grouped 格式
        setCart(data);
      } else {
        const local = localStorage.getItem(LOCAL_KEY);
        setCart(local ? JSON.parse(local) : []);
      }
    } catch {

      showToast("Failed to load cart.");
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
      const updatedCart = cart.map(group => ({
        ...group,
        items: group.items.map(item =>
          item.id === id ? { ...item, quantity: q } : item
        ),
      }));
      saveLocalCart(updatedCart);
    }
  };

  const deleteItem = async (id: string) => {
    if (token) {
      await deleteCartItem(id, token);
      fetchCart();
    } else {
      const updatedCart = cart
        .map(group => ({
          ...group,
          items: group.items.filter(item => item.id !== id),
        }))
        .filter(group => group.items.length > 0); // 移除空组
      saveLocalCart(updatedCart);
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
