import { useParams, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { useCart } from './CartContext';

type Dish = {
  id: number;
  name: string;
  description: string;
  price: number;
};

type Menu = {
  restaurantId: number;
  items: Dish[];
};

const MenuPage = () => {
  const { restaurantId } = useParams();
  const navigate = useNavigate();
  const [menu, setMenu] = useState<Menu | null>(null);
  const { triggerReload } = useCart();

  // 👇 将 restaurantId 从 string | undefined 转成 number 或 null
  const restId = restaurantId ? parseInt(restaurantId) : null;

  useEffect(() => {
    if (!restId) {
      console.error("餐厅 ID 无效");
      return;
    }

    fetch(`/api/menu/restaurant/${restId}`)
      .then((res) => res.json())
      .then((data) => setMenu(data))
      .catch((err) => console.error("获取菜单失败：", err));
  }, [restId]);

  const addToCart = async (dishId: number) => {
    const token = localStorage.getItem("token");

    if (!restId) {
      alert("⚠️ 餐厅 ID 缺失，无法加入购物车");
      return;
    }

    const res = await fetch("/api/cart/add", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
      },
      body: JSON.stringify({
        dishId: dishId.toString(),
        restaurantId: restId.toString(), // ✅ 确保传的是 string 类型
        quantity: 1
      }),
    });

    if (res.ok) {
      alert("✅ 已加入购物车");
      triggerReload();
    } else {
      const text = await res.text();
      alert("❌ 加入失败：" + text);
    }
  };

  if (!menu) return <p>加载中...</p>;

  return (
    <div style={{ padding: "2rem" }}>
      {/* 返回按钮 */}
      <button
        onClick={() => navigate(-1)}
        style={{
          marginBottom: "1.5rem",
          padding: "6px 12px",
          backgroundColor: "#ddd",
          border: "none",
          borderRadius: "5px",
          cursor: "pointer"
        }}
      >
        ← 返回餐厅列表
      </button>

      <h1 style={{ marginBottom: "1.5rem" }}>🍛 餐厅 #{menu.restaurantId} 的菜单</h1>

      <ul style={{ listStyle: "none", padding: 0 }}>
        {menu.items.map((dish) => (
          <li
            key={dish.id}
            style={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              padding: "1rem",
              borderBottom: "1px solid #ccc"
            }}
          >
            <div>
              <strong>{dish.name}</strong> - €{dish.price.toFixed(2)} <br />
              <em style={{ color: "#555" }}>{dish.description}</em>
            </div>

            <button
              style={{
                padding: "6px 10px",
                backgroundColor: "#007bff",
                color: "#fff",
                border: "none",
                borderRadius: "5px",
                cursor: "pointer"
              }}
              onClick={() => addToCart(dish.id)}
            >
              加入购物车
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default MenuPage;
