import { useEffect, useState } from "react";
import axios from "axios";
import { Restaurant } from "../types/restaurant";
import { FilterParams } from "../types/filter";

// 自定义 Hook：根据筛选条件获取餐厅列表
export default function useRestaurants({
  categories,
  prices,
  meals,
  sortOrder,
}: FilterParams) {
  const [restaurants, setRestaurants] = useState<Restaurant[]>([]); // 存放餐厅数据

  useEffect(() => {
    const fetchRestaurants = async () => {
      try {
        const payload = {
          categories: categories.length ? categories : null,
          prices: prices.length ? prices : null,
          meals: meals.length ? meals : null,
          sortOrder,
        };

        const res = await axios.post("/api/restaurants/filter", payload);
        setRestaurants(res.data);
        console.log("✅ 获取到的餐厅数据：", res.data);
      } catch (err) {
        console.error("❌ 获取餐厅失败", err);
      }
    };

    fetchRestaurants();
  }, [JSON.stringify({ categories, prices, meals, sortOrder })]);

  return restaurants;
}
