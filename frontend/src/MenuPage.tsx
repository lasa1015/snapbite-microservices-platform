import { useParams, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';

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

  useEffect(() => {
    fetch(`/api/menu/restaurant/${restaurantId}`)
      .then((res) => res.json())
      .then((data) => setMenu(data))
      .catch((err) => console.error("获取菜单失败：", err));
  }, [restaurantId]);

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
            key={dish.id ?? dish.name}
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
              onClick={() => {
                // 暂不实现，后续做购物车功能时处理
                alert("🛒 功能暂未实现");
              }}
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
