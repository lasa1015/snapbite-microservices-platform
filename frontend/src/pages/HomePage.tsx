// src/pages/HomePage.tsx
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import RestaurantCard from "../components/RestaurantCard";
import useRestaurants from "../hooks/useRestaurants";
import { useUserStore } from "../stores/userStore";
import { useFilterStore } from "../stores/filterStore";

const ALL_CATEGORIES = ["Burgers", "Pizza", "Thai", "Indian", "Chinese", "Japanese", "Korean", "Mexican", "Mediterranean", "Middle Eastern", "Vegan", "Vegetarian"];
const ALL_PRICES = ["€", "€€", "€€€"];
const ALL_MEALS = ["breakfast", "lunch", "dinner", "brunch"];

export default function HomePage() {
  const { username, setUsername } = useUserStore();
  const [authMode, setAuthMode] = useState<"login" | "register" | null>(null);

  const {
    categories, setCategories,
    prices, setPrices,
    meals, setMeals,
    sortOrder, setSortOrder,
    clear
  } = useFilterStore();

  const toggle = (value: string, arr: string[], setArr: (v: string[]) => void) => {
    setArr(arr.includes(value) ? arr.filter(v => v !== value) : [...arr, value]);
  };

  const restaurants = useRestaurants({ categories, prices, meals, sortOrder });

  const buttonClass = (active: boolean) =>
    `px-4 py-2 rounded-full text-sm ${active ? 'bg-red-600 text-white' : 'bg-gray-100 text-gray-700'}`;

  const tagClass = "bg-[#ffffff] text-red-700 font-regular px-0 py-2.5 rounded-xl text-r uppercase font-calsans";

  return (
    <div className="mx-auto px-[9vw] py-8">


      {/* 筛选区域 */}
      <div className="mb-6 space-y-6">
        {/* 分类按钮组 */}
        <div className="space-y-3">
          <div className="flex flex-wrap items-center gap-3 w-full">
            <span className={tagClass}>cuisine</span>
            {ALL_CATEGORIES.map(c => (
              <button
                key={c}
                onClick={() => toggle(c, categories, setCategories)}
                className={buttonClass(categories.includes(c))}
              >{c}</button>
            ))}
          </div>

          <div className="flex flex-wrap items-center gap-4 w-full">
            <span className={tagClass}>time</span>
            {ALL_MEALS.map(m => (
              <button
                key={m}
                onClick={() => toggle(m, meals, setMeals)}
                className={buttonClass(meals.includes(m))}
              >{m}</button>
            ))}
          </div>

          <div className="flex flex-wrap items-center gap-4 w-full">
            <span className={tagClass}>price</span>
            {ALL_PRICES.map(p => (
              <button
                key={p}
                onClick={() => toggle(p, prices, setPrices)}
                className={buttonClass(prices.includes(p))}
              >{p}</button>
            ))}
          </div>

          <div className="flex items-center gap-4 w-full">
            <span className={tagClass}>sort</span>
            <button
              onClick={() => setSortOrder("desc")}
              className={buttonClass(sortOrder === "desc")}
            >Rating ↓</button>
            <button
              onClick={() => setSortOrder("asc")}
              className={buttonClass(sortOrder === "asc")}
            >Rating ↑</button>
          </div>
        </div>
      </div>

      {/* 顶部信息 + Reset */}
      <div className="flex justify-between items-center mb-4">
        <p className="text-2xl  text-gray-800 font-calsans">Found <strong>{restaurants.length}</strong> restaurants</p>
        <button
          onClick={clear}
          className="bg-gray-300 text-sm px-6 py-2 rounded-full hover:bg-gray-400 text-gray-800 font-semibold"
        >Reset</button>
      </div>


      {/* 餐厅展示区域 */}
      <div className="grid grid-cols-4 gap-7">
        {restaurants.map(r => <RestaurantCard key={r.id} restaurant={r} />)}
      </div>
    </div>
  );
}