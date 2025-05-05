// src/hooks/useMerchantOrders.ts
import { useEffect, useState } from "react";
import axios from "axios";

export function useMerchantOrders() {
  const [restaurantInfo, setRestaurantInfo] = useState<any>(null);
  const [orders, setOrders] = useState<any[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetch = async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        setError("未登录或 token 失效");
        setLoading(false);
        return;
      }

      try {
        const me = await axios.get("/api/users/me", {
          headers: { Authorization: `Bearer ${token}` },
        });

        const restaurant = await axios.get(`/api/restaurants/by-user/${me.data.username}`, {
          headers: { Authorization: `Bearer ${token}` },
        });

        setRestaurantInfo(restaurant.data);

        const orderRes = await axios.get(`/api/order/merchant/restaurant/${restaurant.data.id}`, {
          headers: { Authorization: `Bearer ${token}` },
        });

        setOrders(orderRes.data);
      } catch (err: any) {
        setError(err.response?.data || "加载失败");
      } finally {
        setLoading(false);
      }
    };

    fetch();
  }, []);

  return { restaurantInfo, orders, error, loading };
}
