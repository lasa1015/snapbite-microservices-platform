// ✅ src/hooks/useRestaurants.ts

// 获取餐厅数据
// 可以根据筛选条件获取

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
  const [loading, setLoading] = useState(false); // 控制加载状态

  useEffect(() => {
    const fetchRestaurants = async () => {
      setLoading(true);
      try {
        
        // 构造请求体（前端的 null 会被转换成有效字段）
        const payload = {
          categories: categories.length ? categories : null,
          prices: prices.length ? prices : null,
          meals: meals.length ? meals : null,
          sortOrder,
        };

        const res = await axios.post("/api/restaurants/filter", payload);
        setRestaurants(res.data); // 成功后设置餐厅列表

        console.log("✅ 获取到的餐厅数据：", res.data); //  打印餐厅数据
      } catch (err) {
        console.error("❌ 获取餐厅失败", err);
      } finally {
        setLoading(false); // 无论成功失败都关闭 loading 状态
      }
    };

    // JSON.stringify 监听依赖数组变化（防止对象浅比较）
    fetchRestaurants();
  }, [JSON.stringify({ categories, prices, meals, sortOrder })]);

  return restaurants;
}
