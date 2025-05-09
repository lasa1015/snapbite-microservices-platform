import { useEffect, useState } from "react";
import axios from "axios";

export function useMerchantOrders() {
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
        const res = await axios.get("/api/order/merchant/my-restaurant-orders", {
          headers: { Authorization: `Bearer ${token}` },
        });

        setOrders(res.data);
      } catch (err: any) {
        setError(err.response?.data || "加载失败");
      } finally {
        setLoading(false);
      }
    };

    fetch();
  }, []);

  return { orders, error, loading };
}
