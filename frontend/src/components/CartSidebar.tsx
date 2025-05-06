
import { useCartData } from "../hooks/useCartData";
import { useUserStore } from "../stores/userStore";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import AuthModal from "./AuthModal";
import { CartItem } from "../types/cart";
import { useCartStore } from "../stores/cartStore";



export default function CartSidebar() {

  const { closeCart } = useCartStore(); // ✅ 获取关闭函数
  const { reloadFlag, triggerReload } = useCartStore();
  


  const { username, setUsername } = useUserStore();

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
    <div
    className="fixed top-0 right-0 w-[300px] h-screen bg-white border-l shadow-lg z-50 p-4 overflow-y-auto"
  >
    {/* 顶部栏 + 关闭按钮 */}
    <div className="flex justify-between items-center mb-4">
      <h3 className="text-lg font-semibold">🛒 我的购物车</h3>
      <button
        onClick={closeCart}
        className="text-lg text-gray-600 hover:text-black"
      >
        ❌
      </button>
    </div>

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
