import { useEffect, useState } from "react";
import "./App.css";
import RestaurantCard from "./RestaurantCard";
import AuthModal from "./AuthModal";
import CartSidebar from "./CartSidebar"; // 新增购物车侧边栏

const ALL_CATEGORIES = [
  "Burgers", "Pizza", "Thai", "Indian", "Chinese", "Japanese",
  "Korean", "Mexican", "Mediterranean", "Middle Eastern", "Vegan", "Vegetarian"
];
const ALL_PRICES = ["€", "€€", "€€€"];
const ALL_MEALS = ["breakfast", "lunch", "dinner", "brunch"];

type Restaurant = {
  id: number;
  name: string;
  imageUrl: string;
  displayAddress?: string;
  rating: number;
  reviewCount: number;
  price?: string;
  category?: string;
  description?: string;
};

export default function App() {
  const [authMode, setAuthMode] = useState<"login" | "register" | null>(null);
  const [username, setUsername] = useState<string | null>(null);
  const [restaurants, setRestaurants] = useState<Restaurant[]>([]);
  const [categories, setCategories] = useState<string[]>([]);
  const [prices, setPrices] = useState<string[]>([]);
  const [meals, setMeals] = useState<string[]>([]);

  const hasFilters = categories.length > 0 || prices.length > 0 || meals.length > 0;

  const toggle = (value: string, arr: string[], setArr: (v: string[]) => void) => {
    setArr(arr.includes(value) ? arr.filter(v => v !== value) : [...arr, value]);
  };

  const clearFilters = () => {
    setCategories([]);
    setPrices([]);
    setMeals([]);
  };

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      fetch("/api/users/me", {
        headers: { Authorization: `Bearer ${token}` },
      })
        .then(res => {
          if (!res.ok) throw new Error();
          return res.json();
        })
        .then(user => setUsername(user.username))
        .catch(() => {
          localStorage.removeItem("token");
          localStorage.removeItem("username");
          setUsername(null);
        });
    }
  }, []);

  useEffect(() => {
    if (!hasFilters) {
      fetch("/api/restaurants")
        .then(res => res.json())
        .then(setRestaurants)
        .catch(err => console.error("❌ 获取餐厅失败", err));
    } else {
      fetch("/api/restaurants/filter", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          categories: categories.length ? categories : null,
          prices: prices.length ? prices : null,
          meals: meals.length ? meals : null
        })
      })
        .then(res => res.json())
        .then(setRestaurants)
        .catch(err => console.error("❌ 筛选失败", err));
    }
  }, [categories, prices, meals, hasFilters]);

  return (
    <div style={{ display: "flex" }}>
      {/* 左侧主要内容 */}
      <div style={{ flex: 1, padding: "2rem" }}>
        {/* 顶部登录区域 */}
        <div style={{ display: "flex", justifyContent: "flex-end", gap: "1rem", marginBottom: "1rem" }}>
          {username ? (
            <>
              <div>👤 欢迎：{username}</div>
              <button onClick={() => {
                localStorage.removeItem("token");
                localStorage.removeItem("username");
                setUsername(null);
              }}>退出</button>
            </>
          ) : (
            <>
              <button onClick={() => setAuthMode("login")}>登录</button>
              <button onClick={() => setAuthMode("register")}>注册</button>
            </>
          )}
        </div>

        {/* 登录/注册弹窗 */}
        {authMode && (
          <AuthModal
            mode={authMode}
            onClose={() => setAuthMode(null)}
            onLoginSuccess={(name: string) => {
              setUsername(name);
              setAuthMode(null);
            }}
          />
        )}

        <h1>🍽️ Snapbite 餐厅推荐</h1>

        {/* 筛选器区域 */}
        <div style={{ marginBottom: "1.5rem" }}>
          <h3>筛选条件：</h3>

          <div style={{ marginBottom: "0.5rem" }}>
            <strong>类别：</strong>
            {ALL_CATEGORIES.map(c => (
              <button
                key={c}
                onClick={() => toggle(c, categories, setCategories)}
                style={{
                  margin: "4px", padding: "4px 10px",
                  backgroundColor: categories.includes(c) ? "#007bff" : "#eee",
                  color: categories.includes(c) ? "#fff" : "#000",
                  borderRadius: "5px", border: "1px solid #ccc", cursor: "pointer"
                }}>
                {c}
              </button>
            ))}
          </div>

          <div style={{ marginBottom: "0.5rem" }}>
            <strong>价格：</strong>
            {ALL_PRICES.map(p => (
              <button
                key={p}
                onClick={() => toggle(p, prices, setPrices)}
                style={{
                  margin: "4px", padding: "4px 10px",
                  backgroundColor: prices.includes(p) ? "#28a745" : "#eee",
                  color: prices.includes(p) ? "#fff" : "#000",
                  borderRadius: "5px", border: "1px solid #ccc", cursor: "pointer"
                }}>
                {p}
              </button>
            ))}
          </div>

          <div style={{ marginBottom: "0.5rem" }}>
            <strong>用餐时间：</strong>
            {ALL_MEALS.map(m => (
              <button
                key={m}
                onClick={() => toggle(m, meals, setMeals)}
                style={{
                  margin: "4px", padding: "4px 10px",
                  backgroundColor: meals.includes(m) ? "#ffc107" : "#eee",
                  color: "#000",
                  borderRadius: "5px", border: "1px solid #ccc", cursor: "pointer"
                }}>
                {m}
              </button>
            ))}
          </div>

          <button
            onClick={clearFilters}
            style={{ marginTop: "8px", padding: "6px 12px" }}>
            🔄 清除筛选
          </button>
        </div>

        <p style={{ marginBottom: "1rem" }}>
          共找到 <strong>{restaurants.length}</strong> 家餐厅
        </p>

        {/* 餐厅列表展示 */}
        {restaurants.map(r => (
          <RestaurantCard key={r.id} restaurant={r} />
        ))}
      </div>

    
    </div>
  );
}
