import { useEffect, useState } from "react";
import "./App.css";
import RestaurantCard from "./RestaurantCard";

/* 可选项常量 */
const ALL_CATEGORIES = [
  "Burgers", "Pizza", "Thai", "Indian", "Chinese", "Japanese",
  "Korean", "Mexican", "Mediterranean", "Middle Eastern", "Vegan", "Vegetarian"
];
const ALL_PRICES  = ["€", "€€", "€€€"];
const ALL_MEALS   = ["breakfast", "lunch", "dinner", "brunch"];

/* 餐厅类型（和后端字段一致即可） */
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
  const [restaurants, setRestaurants] = useState<Restaurant[]>([]);

  /* 已选筛选条件 */
  const [categories, setCategories] = useState<string[]>([]);
  const [prices,     setPrices]     = useState<string[]>([]);
  const [meals,      setMeals]      = useState<string[]>([]);

  /** 有任何一个筛选数组非空，就视为“有筛选” */
  const hasFilters =
    categories.length > 0 || prices.length > 0 || meals.length > 0;

  /** 通用切换函数 */
  const toggle = (value: string, arr: string[], setArr: (v: string[]) => void) => {
    setArr(arr.includes(value) ? arr.filter(v => v !== value) : [...arr, value]);
  };

  /** 清空所有筛选 */
  const clearFilters = () => {
    setCategories([]);
    setPrices([]);
    setMeals([]);
  };

  /** 拉数据：无筛选 => GET， 有筛选 => POST */
  useEffect(() => {
    if (!hasFilters) {
      /* 初次/清空：直接取全部 */
      fetch("/api/restaurants")
        .then(res => res.json())
        .then(setRestaurants)
        .catch(err => console.error("❌ 获取餐厅失败", err));
    } else {
      /* 有筛选：向 /filter 发送条件 */
      fetch("/api/restaurants/filter", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          categories: categories.length ? categories : null,
          prices:     prices.length     ? prices     : null,
          meals:      meals.length      ? meals      : null
        })
      })
        .then(res => res.json())
        .then(setRestaurants)
        .catch(err => console.error("❌ 筛选失败", err));
    }
  }, [categories, prices, meals, hasFilters]);

  /* ===== 视图区域 ===== */
  return (
    <div style={{ padding: "2rem" }}>
      <h1>🍽️ Snapbite 餐厅推荐</h1>

      {/* ---- 筛选器 ---- */}
      <div style={{ marginBottom: "1.5rem" }}>
        <h3>筛选条件：</h3>

        {/* 类别 */}
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

        {/* 价格 */}
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

        {/* 用餐时间 */}
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

        {/* 清除按钮 */}
        <button
          onClick={clearFilters}
          style={{ marginTop: "8px", padding: "6px 12px" }}>
          🔄 清除筛选
        </button>
      </div>

      {/* 列表 */}
      {restaurants.map(r => (
        <RestaurantCard key={r.id} restaurant={r} />
      ))}
    </div>
  );
}
