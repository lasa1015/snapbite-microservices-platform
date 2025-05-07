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
    window.location.reload();
  };

  const handleCancel = async (id: string) => {
    const token = localStorage.getItem("token");
    await axios.patch(`/api/order/merchant/cancel/${id}`, {}, {
      headers: { Authorization: `Bearer ${token}` },
    });
    window.location.reload();
  };

  if (loading) return <p className="text-center mt-10 text-gray-600">Loading…</p>;
  if (error) return <p className="text-center mt-10 text-red-600">{error}</p>;

  return (
    <div className="max-w-screen-xl mx-auto px-4 py-10">

      {/* Restaurant Info */}
      {restaurantInfo && (
        <div className="max-w-[800px] mx-auto px-4 mb-6">
          <h2 className="text-[28px] font-[600] font-outfit mb-1">{restaurantInfo.name}</h2>
          <p className="text-sm text-gray-600">{restaurantInfo.displayAddress}</p>
        </div>
      )}

      {/* Order List */}
      <div className="space-y-6">
        {orders.length === 0 ? (
          <p className="text-gray-500 text-center">No orders yet.</p>
        ) : (
          orders.map(order => (
            <MerchantOrderCard
              key={order.id}
              order={order}
              onShip={handleShip}
              onCancel={handleCancel}
            />
          ))
        )}
      </div>
    </div>
  );
}
