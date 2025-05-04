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
  dishes: Dish[];
};

type Restaurant = {
  id: number;
  name: string;
  imgUrl: string;
  displayAddress: string;
  rating: number;
  price: string;
  category: string;
  description: string;
};

type CartItem = {
  id: string;
  restaurantId: string;
  dishId: string;
  dishName?: string;
  price?: number;
  quantity: number;
};

const LOCAL_KEY = "guest_cart";

const MenuPage = () => {
  const { restaurantId } = useParams();
  const navigate = useNavigate();
  const { triggerReload } = useCart();
  const [menu, setMenu] = useState<Menu | null>(null);
  const [restaurant, setRestaurant] = useState<Restaurant | null>(null);

  const restId = restaurantId ? parseInt(restaurantId) : null;
  const token = localStorage.getItem("token");

  useEffect(() => {
    if (!restId) return;

    // 获取菜单
    fetch(`/api/menu/restaurant/${restId}`)
      .then(res => res.json())
      .then(setMenu)
      .catch(err => console.error("获取菜单失败：", err));

    // 获取餐厅信息
    fetch(`/api/restaurants/${restId}`, {
      method: "POST"
    })
      .then(res => res.json())
      .then(setRestaurant)
      .catch(err => console.error("获取餐厅信息失败：", err));
  }, [restId]);

  const addToCart = async (dishId: number) => {
    if (!restId || !menu) return alert("⚠️ 餐厅或菜单数据缺失");

    const dish = menu.dishes.find(d => d.id === dishId);
    if (!dish) return alert("⚠️ 找不到该菜品");

    // ✅ 登录用户：发请求给后端
    if (token) {
      const res = await fetch("/api/cart/add", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
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

    } else {
      // ❌ 未登录用户：操作 localStorage
      const local = localStorage.getItem(LOCAL_KEY);
      let cart: CartItem[] = local ? JSON.parse(local) : [];

      const existing = cart.find(
        item => item.dishId === dishId.toString() && item.restaurantId === restId.toString()
      );

      if (existing) {
        existing.quantity += 1;
      } else {
        cart.push({
          id: crypto.randomUUID(),
          dishId: dishId.toString(),
          restaurantId: restId.toString(),
          dishName: dish.name,
          price: dish.price,
          quantity: 1,
        });
      }

      localStorage.setItem(LOCAL_KEY, JSON.stringify(cart));
      alert("✅ 已加入购物车（未登录）");
      triggerReload();
    }
  };

  if (!menu || !restaurant) return <p>加载中...</p>;

  return (
    <div style={{ padding: "2rem" }}>
      <button onClick={() => navigate(-1)} style={{ marginBottom: "1.5rem" }}>← 返回</button>

      {/* 餐厅信息展示部分 */}
      <div style={{ marginBottom: "2rem" }}>
        <img src={restaurant.imgUrl} alt={restaurant.name} style={{ width: "100%", maxHeight: "300px", objectFit: "cover", borderRadius: "10px" }} />
        <h1>{restaurant.name}</h1>
        <p>{restaurant.displayAddress}</p>
        <p>⭐ {restaurant.rating} / 5 · {restaurant.price} · {restaurant.category}</p>
        <p style={{ fontStyle: "italic", color: "#555" }}>{restaurant.description}</p>
      </div>

      {/* 菜单展示部分 */}
      <ul style={{ listStyle: "none", padding: 0 }}>
        {menu.dishes.map(dish => (
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
