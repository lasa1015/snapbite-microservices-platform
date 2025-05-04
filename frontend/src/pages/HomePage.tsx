import { useState } from "react";
import AuthModal from "../components/AuthModal";
import RestaurantCard from "../components/RestaurantCard";
import useRestaurants from "../hooks/useRestaurants";
import { useUser } from "../context/UserContext";

const ALL_CATEGORIES = [ "Burgers", "Pizza", "Thai", "Indian", "Chinese", "Japanese", "Korean", "Mexican", "Mediterranean", "Middle Eastern", "Vegan", "Vegetarian" ];
const ALL_PRICES = ["€", "€€", "€€€"];
const ALL_MEALS = ["breakfast", "lunch", "dinner", "brunch"];

export default function HomePage() {
  
  const [authMode, setAuthMode] = useState<"login" | "register" | null>(null);
  const { username, setUsername } = useUser();
  const [categories, setCategories] = useState<string[]>([]);
  const [prices, setPrices] = useState<string[]>([]);
  const [meals, setMeals] = useState<string[]>([]);
  const [sortOrder, setSortOrder] = useState("desc");

  const toggle = (value: string, arr: string[], setArr: (v: string[]) => void) => {
    setArr(arr.includes(value) ? arr.filter(v => v !== value) : [...arr, value]);
  };

  const clearFilters = () => {
    setCategories([]);
    setPrices([]);
    setMeals([]);
  };

  const restaurants = useRestaurants({
    categories,
    prices,
    meals,
    sortOrder,
  });
  

  return (
    <div style={{ padding: "2rem" }}>
      <div style={{ display: "flex", justifyContent: "flex-end", gap: "1rem", marginBottom: "1rem" }}>
        {username ? (
          <>
            <div>👤 欢迎：{username}</div>
            <button onClick={() => {
              localStorage.clear();
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

      {authMode && <AuthModal mode={authMode} onClose={() => setAuthMode(null)} />}

      <h1>🍽️ Snapbite 餐厅推荐</h1>

      <div style={{ marginBottom: "1.5rem" }}>
        <h3>筛选条件：</h3>

        <div>
          <strong>类别：</strong>
          {ALL_CATEGORIES.map(c => (
            <button key={c} onClick={() => toggle(c, categories, setCategories)}
              style={{ margin: 4, padding: "4px 10px", backgroundColor: categories.includes(c) ? "#007bff" : "#eee", color: categories.includes(c) ? "#fff" : "#000", borderRadius: 5 }}>
              {c}
            </button>
          ))}
        </div>

        <div>
          <strong>价格：</strong>
          {ALL_PRICES.map(p => (
            <button key={p} onClick={() => toggle(p, prices, setPrices)}
              style={{ margin: 4, padding: "4px 10px", backgroundColor: prices.includes(p) ? "#28a745" : "#eee", color: prices.includes(p) ? "#fff" : "#000", borderRadius: 5 }}>
              {p}
            </button>
          ))}
        </div>

        <div>
          <strong>用餐时间：</strong>
          {ALL_MEALS.map(m => (
            <button key={m} onClick={() => toggle(m, meals, setMeals)}
              style={{ margin: 4, padding: "4px 10px", backgroundColor: meals.includes(m) ? "#ffc107" : "#eee", borderRadius: 5 }}>
              {m}
            </button>
          ))}
        </div>

        <div>
          <strong>评分排序：</strong>
          <select value={sortOrder} onChange={e => setSortOrder(e.target.value)} style={{ marginLeft: 8 }}>
            <option value="desc">从高到低</option>
            <option value="asc">从低到高</option>
          </select>
        </div>

        <button onClick={clearFilters} style={{ marginTop: 8, padding: "6px 12px" }}>🔄 清除筛选</button>
      </div>

      <p>共找到 <strong>{restaurants.length}</strong> 家餐厅</p>

      {restaurants.map(r => <RestaurantCard key={r.id} restaurant={r} />)}
    </div>
  );
}
