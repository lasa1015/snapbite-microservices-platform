import MerchantOrderCard from "../components/MerchantOrderCard";
import { useMerchantOrders } from "../hooks/useMerchantOrders";
import axios from "axios";

export default function MerchantDashboard() {
  const { restaurantInfo, orders, error, loading } = useMerchantOrders();

  const handleShip = async (id: string) => {
    const token = localStorage.getItem("token");
    await axios.patch(`/api/order/merchant/ship/${id}`, {}, {
      headers: { Authorization: `Bearer ${token}` },
    });
    window.location.reload(); // ✅ 可以优化为局部更新
  };

  const handleCancel = async (id: string) => {
    const token = localStorage.getItem("token");
    await axios.patch(`/api/order/merchant/cancel/${id}`, {}, {
      headers: { Authorization: `Bearer ${token}` },
    });
    window.location.reload();
  };

  if (loading) return <p>加载中...</p>;
  if (error) return <p style={{ color: "red" }}>{error}</p>;

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
      {orders.length === 0 ? <p>暂无订单</p> : orders.map(order => (
        <MerchantOrderCard
          key={order.id}
          order={order}
          onShip={handleShip}
          onCancel={handleCancel}
        />
      ))}
    </div>
  );
}
