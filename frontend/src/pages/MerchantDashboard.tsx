import MerchantOrderCard from "../components/MerchantOrderCard";
import { useMerchantOrders } from "../hooks/useMerchantOrders";
import axios from "axios";

export default function MerchantDashboard() {
  const { orders, error, loading } = useMerchantOrders();

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

  const handleAccept = async (id: string) => {
    const token = localStorage.getItem("token");
    await axios.patch(`/api/order/merchant/accept/${id}`, {}, {
      headers: { Authorization: `Bearer ${token}` },
    });
    window.location.reload();
  };

  const handleConfirm = async (id: string) => {
    const token = localStorage.getItem("token");
    await axios.patch(`/api/order/merchant/confirm/${id}`, {}, {
      headers: { Authorization: `Bearer ${token}` },
    });
    window.location.reload();
  };

  if (loading) return <p className="text-center mt-10 text-gray-600">Loading…</p>;
  if (error) return <p className="text-center mt-10 text-red-600">{error}</p>;

  return (
    <div className="max-w-screen-xl mx-auto px-4 py-10">

      {/* Restaurant Info */}
      {orders.length > 0 && (
        <div className="max-w-[800px] mx-auto px-4 mb-6">
          <h2 className="text-[28px] font-[600] font-outfit mb-1 underline underline-offset-2">
           Restaurant:  {orders[0].restaurantName}
          </h2>
          <p className=" text-gray-900 text-[22px] font-outfit mt-4">You have {orders.length} orders.</p>
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
              onAccept={handleAccept} 
              onConfirm={handleConfirm}
            />
          ))
        )}
      </div>
    </div>
  );
}