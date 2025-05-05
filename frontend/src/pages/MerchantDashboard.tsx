// src/pages/MerchantDashboard.tsx
import { useEffect, useState } from "react";
import axios from "axios";

export default function MerchantDashboard() {
  const [orders, setOrders] = useState([]);
  const [restaurantInfo, setRestaurantInfo] = useState<any>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        setError("未登录或 token 失效");
        setLoading(false);
        return;
      }

      try {
        // 1. 获取当前商户信息
        const meRes = await axios.get("/api/users/me", {
          headers: { Authorization: `Bearer ${token}` },
        });

        const user = meRes.data;

        // 2. 获取商户绑定的餐厅信息
        const restaurantRes = await axios.get(`/api/restaurants/by-user/${user.username}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        const restaurant = restaurantRes.data;
        setRestaurantInfo(restaurant);

        // 3. 获取该餐厅的订单列表
        const ordersRes = await axios.get(`/api/order/merchant/restaurant/${restaurant.id}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setOrders(ordersRes.data);
      } catch (err: any) {
        console.error("加载失败：", err);
        setError(err.response?.data || "加载失败，请检查权限或网络");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const handleShip = async (orderId: string) => {
    const token = localStorage.getItem("token");
    await axios.patch(`/api/order/merchant/ship/${orderId}`, {}, {
      headers: { Authorization: `Bearer ${token}` },
    });
    window.location.reload();
  };

  const handleCancel = async (orderId: string) => {
    const token = localStorage.getItem("token");
    await axios.patch(`/api/order/merchant/cancel/${orderId}`, {}, {
      headers: { Authorization: `Bearer ${token}` },
    });
    window.location.reload();
  };

  if (loading) return <div style={{ padding: "1rem" }}>加载中...</div>;
  if (error) return <div style={{ padding: "1rem", color: "red" }}>{error}</div>;

  return (
    <div style={{ padding: "1rem" }}>
      <h2>商户后台</h2>

      {restaurantInfo && (
        <div style={{ marginBottom: "1rem" }}>
          <h3>餐厅信息</h3>
          <p>名称：{restaurantInfo.name}</p>
          <p>地址：{restaurantInfo.displayAddress}</p>
        </div>
      )}

      <h3>订单列表</h3>
      {orders.length === 0 && <p>暂无订单</p>}
      {orders.map((order: any) => (
        <div key={order.id} style={{ border: "1px solid #ccc", padding: "1rem", marginBottom: "1rem" }}>
          <p>订单号：{order.id}</p>
          <p>收件人：{order.recipient}</p>
          <p>地址：{order.address}</p>
          <p>电话：{order.phone}</p>
          <p>创建时间：{new Date(order.createdAt).toLocaleString()}</p>
          <p>总金额：¥{order.totalPrice}</p>
          <p>状态：{order.status}</p>
          <p>订单内容：</p>
          <ul>
            {order.items.map((item: any, index: number) => (
              <li key={index}>
                {item.dishName} × {item.quantity}（单价：¥{item.price}）
              </li>
            ))}
          </ul>

          {order.status === "CREATED" && (
            <>
              <button onClick={() => handleShip(order.id)}>发货</button>
              <button onClick={() => handleCancel(order.id)} style={{ marginLeft: "0.5rem" }}>取消订单</button>
            </>
          )}
        </div>
      ))}
    </div>
  );
}
