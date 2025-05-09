// src/pages/RestaurantsPage.tsx

import RestaurantCard from "../components/RestaurantCard";
import useRestaurants from "../hooks/useRestaurants";
import { useFilterStore } from "../stores/filterStore";
import { useEffect } from "react"; 

const ALL_CATEGORIES = ["Burgers", "Pizza", "Thai", "Indian", "Chinese", "Japanese", "Korean", "Mexican", "Mediterranean", "Middle Eastern", "Vegan", "Vegetarian"];
const ALL_PRICES = ["€", "€€", "€€€"];
const ALL_MEALS = ["breakfast", "lunch", "dinner", "brunch"];

export default function HomePage() {


  const {
    categories, setCategories,
    prices, setPrices,
    meals, setMeals,
    sortOrder, setSortOrder,
    clear
  } = useFilterStore();


 // ✅ 每当筛选条件变化时，打印到控制台
  useEffect(() => {
    console.log("当前筛选条件：", {
      categories,
      prices,
      meals,
      sortOrder
    });
  }, [categories, prices, meals, sortOrder]);



  const toggle = (value: string, arr: string[], setArr: (v: string[]) => void) => {
    setArr(arr.includes(value) ? arr.filter(v => v !== value) : [...arr, value]);
  };

  // 调用自定义 Hook 获取餐厅数据
  // 传入筛选条件：分类、价格、用餐时间和排序方式
  const restaurants = useRestaurants({ categories, prices, meals, sortOrder });



const buttonClass = (active: boolean) =>
  `px-[10px] py-[7px] min-w-[80px] text-center rounded-full text-sm border-[1.7px] transition ${
    active
      ? 'bg-white text-primary border-primary hover:bg-red-50'
      : 'bg-gray-100 text-gray-700 border-gray-100 hover:border-gray-300'
  }`;


  const tagClass = "bg-[#ffffff] text-gray-500 font-[600] pr-3 py-2.5 rounded-xl text-[13px] uppercase font-outfit";

  return (

    <div className="mx-auto px-0 py-6 max-w-screen-xl">



      {/* 筛选区域 */}
      <div className="mb-6 space-y-6">


        {/* 分类按钮组 */}
        <div className="space-y-3">
          
          <div className="flex flex-wrap items-center gap-2 w-full">
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
      <p className="text-[26px] text-gray-800 font-outfit font-[350]">
  Found <span className="font-[600]">{restaurants.length}</span> restaurants
</p>

        <button
  onClick={clear}
  className="w-20 h-9 rounded-full bg-gray-100  hover:bg-gray-200 flex items-center justify-center font-[400]  text-[15px] text-gray-700"
>
  Reset
</button>

      
      </div>


      {/* 餐厅展示区域 */}
      <div className="grid grid-cols-4 gap-7">
        {restaurants.map(r => <RestaurantCard key={r.id} restaurant={r} />)}
      </div>
    </div>
  );
}