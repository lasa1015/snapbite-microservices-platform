import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { useCart } from "../context/CartContext";

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
  const { triggerReload } = useCart();
  const [menu, setMenu] = useState<Menu | null>(null);

  const restId = restaurantId ? parseInt(restaurantId) : null;

  useEffect(() => {
    if (!restId) return;
    fetch(`/api/menu/restaurant/${restId}`)
      .then(res => res.json())
      .then(setMenu)
      .catch(err => console.error("获取菜单失败：", err));
  }, [restId]);

  const addToCart = async (dishId: number) => {
    const token = localStorage.getItem("token");
    if (!restId) return alert("⚠️ 餐厅 ID 缺失");

    const res = await fetch("/api/cart/add", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
      },
      body: JSON.stringify({
        dishId: dishId.toString(),
        restaurantId: restId.toString(),
        quantity: 1
      }),
    });

    if (res.ok) {
      alert("✅ 已加入购物车");
      triggerReload();
    } else {
      alert("❌ 加入失败：" + await res.text());
    }
  };

  if (!menu) return <p>加载中...</p>;

  return (
    <div style={{ padding: "2rem" }}>
      <button onClick={() => navigate(-1)} style={{ marginBottom: "1.5rem" }}>← 返回</button>
      <h1>🍛 餐厅 #{menu.restaurantId} 的菜单</h1>
      <ul style={{ listStyle: "none", padding: 0 }}>
        {menu.items.map(dish => (
          <li key={dish.id} style={{ display: "flex", justifyContent: "space-between", padding: "1rem", borderBottom: "1px solid #ccc" }}>
            <div>
              <strong>{dish.name}</strong> - €{dish.price.toFixed(2)}<br />
              <em>{dish.description}</em>
            </div>
            <button onClick={() => addToCart(dish.id)}>加入购物车</button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default MenuPage;
