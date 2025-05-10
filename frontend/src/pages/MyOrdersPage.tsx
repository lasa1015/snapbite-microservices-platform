import { useEffect, useState } from "react";
import OrderCard from "../components/OrderCard";
import { Order } from "../types/order";
import { useToast } from "../components/Toast";

export default function MyOrdersPage() {
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const showToast = useToast();

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

      if (!res.ok) throw new Error("Failed to fetch orders");
      const data = await res.json();
      setOrders(data);
    } catch (err) {
      console.error("Failed to fetch orders", err);
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = async (orderId: string) => {
    const token = localStorage.getItem("token");
    if (!token) return alert("Not logged in");

    const res = await fetch(`/api/order/cancel/${orderId}`, {
      method: "PATCH",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (res.ok) {

      showToast(" Order cancelled");

      setOrders(prev =>
        prev.map(order =>
          order.id === orderId ? { ...order, status: "CANCELED" } : order
        )
      );
    } else {
      const errMsg = await res.text();

      showToast("❌ Cancel failed: " + errMsg);
    }
  };

  const handleConfirm = async (orderId: string) => {
    const token = localStorage.getItem("token");
    if (!token) return alert("Not logged in");

    const res = await fetch(`/api/order/confirm/${orderId}`, {
      method: "PATCH",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (res.ok) {

      showToast(" Order confirmed");
      setOrders(prev =>
        prev.map(order =>
          order.id === orderId ? { ...order, status: "COMPLETED" } : order
        )
      );
    } else {
      const errMsg = await res.text();
      showToast("❌ Confirmation failed: " + errMsg);
    }
  };

  if (loading) return <p>Loading…</p>;
  if (orders.length === 0) return <p>You don’t have any orders yet.</p>;

  return (
    <div className="max-w-screen-xl mx-auto px-2 py-6">

<div className="max-w-[800px] mx-auto px-2">
  <h2 className="text-[28px] font-[500] font-outfit mb-8 underline underline-offset-2">
    My Orders</h2>
</div>


      <div className="space-y-6">
        {orders.map(order => (
          <OrderCard
            key={order.id}
            order={order}
            onCancel={handleCancel}
            onConfirm={handleConfirm}
          />
        ))}
      </div>
    </div>
  );
}
