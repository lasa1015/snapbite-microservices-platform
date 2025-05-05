// src/hooks/useRestaurants.ts
import { useEffect, useState } from "react";
import axios from "axios";
import { Restaurant } from "../types/Restaurant";
import { FilterOptions } from "../types/FilterOptions";

export default function useRestaurants({
  categories,
  prices,
  meals,
  sortOrder,
}: FilterOptions) {
  const [restaurants, setRestaurants] = useState<Restaurant[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchRestaurants = async () => {
      setLoading(true);
      try {
        const payload = {
          categories: categories.length ? categories : null,
          prices: prices.length ? prices : null,
          meals: meals.length ? meals : null,
          sortOrder,
        };

        const res = await axios.post("/api/restaurants/filter", payload);
        setRestaurants(res.data);
      } catch (err) {
        console.error("❌ 获取餐厅失败", err);
      } finally {
        setLoading(false);
      }
    };

    fetchRestaurants();
  }, [JSON.stringify({ categories, prices, meals, sortOrder })]);

  return restaurants;
}
