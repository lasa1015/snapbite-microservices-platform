import { useEffect, useState } from "react";

type Restaurant = {
  id: number;
  name: string;
  imgUrl: string;
  displayAddress?: string;
  rating: number;
  reviewCount: number;
  price?: string;
  category?: string;
  description?: string;
};

type FilterOptions = {
  categories: string[]; // 允许传空数组
  prices: string[];
  meals: string[];
  sortOrder: string;
};

export default function useRestaurants({
  categories,
  prices,
  meals,
  sortOrder,
}: FilterOptions) {
  const [restaurants, setRestaurants] = useState<Restaurant[]>([]);

  useEffect(() => {
    const payload = {
      categories: categories.length ? categories : null,
      prices: prices.length ? prices : null,
      meals: meals.length ? meals : null,
      sortOrder,
    };

    fetch("/api/restaurants/filter", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    })
      .then((res) => res.json())
      .then(setRestaurants)
      .catch((err) => console.error("❌ 获取餐厅失败", err));
  }, [JSON.stringify({ categories, prices, meals, sortOrder })]);

  return restaurants;
}
