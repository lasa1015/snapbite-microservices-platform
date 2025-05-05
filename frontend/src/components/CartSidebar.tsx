import { useCart } from "../context/CartContext";
import { useCartData, CartItem } from "../hooks/useCartData";
import { useUser } from "../context/UserContext";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import AuthModal from "./AuthModal";

export default function CartSidebar() {
  const { reloadFlag, triggerReload } = useCart();
  const { username, setUsername } = useUser();
  const { cart, loading, updateQuantity, deleteItem, clearCart } = useCartData(reloadFlag);

  const [authMode, setAuthMode] = useState<"login" | "register" | null>(null);
  const navigate = useNavigate();

  const LOCAL_KEY = "guest_cart";

  // 🧠 登录成功后，把 localStorage 中的购物车同步到后端
  const syncCart = async () => {
    const token = localStorage.getItem("token");
    if (!token) return;

    const local = localStorage.getItem(LOCAL_KEY);
    if (!local) return;

    const guestItems: CartItem[] = JSON.parse(local);

    for (const item of guestItems) {
      await fetch("/api/cart/add", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`
        },
        body: JSON.stringify({
          dishId: item.dishId,
          restaurantId: item.restaurantId,
          quantity: item.quantity
        })
      });
    }

    // 同步完成，清空本地购物车
    localStorage.removeItem(LOCAL_KEY);
    triggerReload();
  };

  // 🛒 结算按钮点击逻辑
  const handleCheckout = async () => {
    if (!username) {
      setAuthMode("login"); // 未登录 → 弹出登录框
    } else {
      navigate("/checkout"); // 已登录 → 进入确认订单页
    }
  };

  // 登录成功后的回调逻辑
  const onLoginSuccess = async (username: string) => {
    setUsername(username);
    await syncCart();      // 同步游客购物车
    setAuthMode(null);     // 关闭登录框
    navigate("/checkout"); // 跳转到结算页
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
        <button
          onClick={handleCheckout}
          style={{ background: "#007bff", color: "#fff" }}
        >结算</button>
      </div>

      {authMode && (
        <AuthModal
          mode={authMode}
          onClose={() => setAuthMode(null)}
          onLoginSuccess={onLoginSuccess}
        />
      )}
    </div>
  );
}
