import { useEffect, useState } from "react";
import OrderCard from "../components/OrderCard";

type OrderItem = {
  dishName: string;
  quantity: number;
  price: number;
};

type Order = {
  id: string;
  createdAt: string;
  status: string;
  recipient: string;
  phone: string;
  address: string;
  totalPrice: number;
  items: OrderItem[];
};

export default function MyOrdersPage() {
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    const token = localStorage.getItem("token");
    if (!token) return;

    try {
      const res = await fetch("/api/order/my-orders", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!res.ok) throw new Error("获取失败");
      const data = await res.json();
      setOrders(data);
    } catch (err) {
      console.error("获取订单失败", err);
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = async (orderId: string) => {
    const token = localStorage.getItem("token");
    if (!token) return alert("未登录");

    const res = await fetch(`/api/order/cancel/${orderId}`, {
      method: "PATCH",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (res.ok) {
      alert("✅ 订单已取消");
      // 更新状态为取消
      setOrders(prev =>
        prev.map(order =>
          order.id === orderId ? { ...order, status: "CANCELED" } : order
        )
      );
    } else {
      const errMsg = await res.text();
      alert("❌ 取消失败：" + errMsg);
    }
  };

  if (loading) return <p>加载中...</p>;
  if (orders.length === 0) return <p>你还没有任何订单。</p>;

  return (
    <div style={{ padding: "2rem" }}>
      <h2>📦 我的订单</h2>
      {orders.map(order => (
        <OrderCard key={order.id} order={order} onCancel={handleCancel} />
      ))}
    </div>
  );
}
