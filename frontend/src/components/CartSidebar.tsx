import { useEffect, useState } from "react";
import { useCart } from "../context/CartContext";

type CartItem = {
  id: string;
  restaurantId: string;
  restaurantName?: string;
  dishId: string;
  dishName?: string;
  price?: number;
  quantity: number;
};

const LOCAL_KEY = "guest_cart";

export default function CartSidebar() {
  const { reloadFlag } = useCart();
  const [cart, setCart] = useState<CartItem[]>([]);
  const [loading, setLoading] = useState(false);
  const token = localStorage.getItem("token");

  // ✅ 更新本地购物车并同步到 UI
  const saveLocalCart = (items: CartItem[]) => {
    localStorage.setItem(LOCAL_KEY, JSON.stringify(items));
    setCart(items);
  };

  // ✅ 根据登录状态读取购物车
  const fetchCart = () => {
    setLoading(true);

    if (token) {
      fetch("/api/cart", {
        headers: { Authorization: `Bearer ${token}` },
      })
        .then((r) => r.json())
        .then(setCart)
        .catch(() => alert("🛒 获取购物车失败"))
        .finally(() => setLoading(false));
    } else {
      try {
        const local = localStorage.getItem(LOCAL_KEY);
        if (local) {
          const data: CartItem[] = JSON.parse(local);
          setCart(data);
        } else {
          setCart([]);
        }
      } catch {
        setCart([]);
      } finally {
        setLoading(false);
      }
    }
  };

  useEffect(fetchCart, [reloadFlag]);

  // ✅ 更新数量
  const updateQuantity = async (id: string, q: number) => {
    if (q < 1) return;

    if (token) {
      await fetch(`/api/cart/${id}/quantity?quantity=${q}`, { method: "PUT" });
      fetchCart();
    } else {
      const newCart = cart.map(item =>
        item.id === id ? { ...item, quantity: q } : item
      );
      saveLocalCart(newCart);
    }
  };

  // ✅ 删除项
  const deleteItem = async (id: string) => {
    if (token) {
      await fetch(`/api/cart/${id}`, { method: "DELETE" });
      fetchCart();
    } else {
      const newCart = cart.filter(item => item.id !== id);
      saveLocalCart(newCart);
    }
  };

  // ✅ 清空购物车
  const clearCart = async () => {
    if (token) {
      await fetch("/api/cart/clear", {
        method: "DELETE",
        headers: token ? { Authorization: `Bearer ${token}` } : {},
      });
      fetchCart();
    } else {
      saveLocalCart([]);
    }
  };

  const grouped = cart.reduce((acc, c) => {
    const key = c.restaurantId;
    (acc[key] ||= { name: c.restaurantName ?? `餐厅 ${key}`, items: [] });
    acc[key].items.push(c);
    return acc;
  }, {} as Record<string, { name: string; items: CartItem[] }>);

  const total = cart.reduce((s, c) => s + (c.price ?? 0) * c.quantity, 0);

  return (
    <div style={{
      width: 300, background: "#f9f9f9", borderLeft: "1px solid #ccc",
      padding: "1rem", height: "100vh", overflowY: "auto", position: "sticky", top: 0
    }}>
      <h3>🛒 我的购物车</h3>
      {loading ? <p>加载中…</p> :
        cart.length === 0 ? <p>暂无商品</p> :
          Object.values(grouped).map(g => (
            <div key={g.name} style={{ marginBottom: 18 }}>
              <h4 style={{ borderBottom: "1px solid #ddd", paddingBottom: 4 }}>{g.name}</h4>
              <ul style={{ listStyle: "none", padding: 0 }}>
                {g.items.map(it => (
                  <li key={it.id} style={{ marginBottom: 12 }}>
                    <div>
                      <strong>{it.dishName ?? "未知菜品"}</strong>{" "}
                      €{(it.price ?? 0).toFixed(2)} × {it.quantity}
                    </div>
                    <div style={{ display: "flex", gap: 6, marginTop: 6 }}>
                      <button onClick={() => updateQuantity(it.id, it.quantity - 1)}>-</button>
                      <button onClick={() => updateQuantity(it.id, it.quantity + 1)}>+</button>
                      <button onClick={() => deleteItem(it.id)}>删除</button>
                    </div>
                  </li>
                ))}
              </ul>
            </div>
          ))}
      <hr />
      <p><strong>总计：</strong> €{total.toFixed(2)}</p>
      <div style={{ display: "flex", justifyContent: "space-between" }}>
        <button onClick={clearCart}>清空</button>
        <button style={{ background: "#007bff", color: "#fff" }}>结算</button>
      </div>
    </div>
  );
}
