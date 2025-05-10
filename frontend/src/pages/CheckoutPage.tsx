import { useCartData } from "../hooks/useCartData";
import { useCartStore } from "../stores/cartStore";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useToast } from "../components/Toast"; // ✅ 新增

export default function CheckoutPage() {
  const { reloadFlag } = useCartStore();
  const { cart } = useCartData(reloadFlag);
  const navigate = useNavigate();
  const showToast = useToast(); // ✅ 使用 Toast
  const [recipient, setRecipient] = useState("");
  const [phone, setPhone] = useState("");
  const [address, setAddress] = useState("");
const { closeCart } = useCartStore();


  const total = cart
    .flatMap(group => group.items)
    .reduce((sum, item) => sum + (item.dishPrice || 0) * item.quantity, 0);

  const handleSubmit = async () => {
    if (!address || !recipient || !phone) {

      showToast("Please complete all delivery details");
      return;
    }

    const token = localStorage.getItem("token");
    if (!token) return alert("Not logged in");

    const res = await fetch("/api/order/checkout", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ address, recipient, phone }),
    });

    if (res.ok) {

      showToast("Order placed successfully!");
       closeCart(); // ✅ 主动关闭购物车
      navigate("/my-orders");
    } else {
      showToast("❌ Order failed: " + (await res.text()));
    }
  };

  return (
    <div className="max-w-[850px] mx-auto px-6 py-4">
      <h2 className="text-[28px] font-[500] font-outfit mb-8 underline underline-offset-4">
        Confirm Your Order Details
      </h2>

      {/* ✅ 直接遍历 cart，每项就是一个餐厅 */}
      {cart.map((group) => (
        <div key={group.restaurantId} className="mb-6 border-b pb-4">
          <h3 className="text-[24px] font-[500] mb-2 font-outfit">{group.restaurantName}</h3>
          {group.items.map((item) => {
            const imagePath = `/images/dish_images/${group.restaurantId}/${item.dishId}.jpg`;
            return (
              <div key={item.id} className="flex items-center justify-between mb-2">
                <div className="flex items-center">
                  <img
                    src={imagePath}
                    alt={item.dishName}
                    className="w-24 h-24 rounded-md object-cover mr-3 mb-3"
                    onError={(e) => {
                      e.currentTarget.onerror = null;
                      e.currentTarget.src = "/images/placeholder.jpg";
                    }}
                  />
                  <div>
                    <div className="text-[20px] font-outfit">{item.dishName}</div>
                    <div className="text-m text-gray-500">× {item.quantity}</div>
                  </div>
                </div>
                <div className="text-right font-medium">
                  €{((item.dishPrice||  0) * item.quantity).toFixed(2)}
                </div>
              </div>
            );
          })}
        </div>
      ))}

      {/* ✅ 小计 */}
      <div className="flex justify-between pb-4 font-[400] text-[24px] mb-6 font-outfit">
        <span>Subtotal</span>
        <span>€{total.toFixed(2)}</span>
      </div>

      {/* ✅ 配送信息表单 */}
      <div className="space-y-4 mb-6">
        <h3 className="font-semibold font-outfit text-[24px]">Delivery Details</h3>
        <div className="flex gap-4">
          <input
            value={recipient}
            onChange={(e) => setRecipient(e.target.value)}
            placeholder="Name"
            className="flex-1 border px-4 py-2 rounded bg-gray-100"
          />
          <input
            value={phone}
            onChange={(e) => setPhone(e.target.value)}
            placeholder="Phone"
            className="flex-1 border px-4 py-2 rounded bg-gray-100"
          />
        </div>
        <input
          value={address}
          onChange={(e) => setAddress(e.target.value)}
          placeholder="Address"
          className="w-full border px-4 py-2 rounded bg-gray-100"
        />
      </div>

      {/* ✅ 提交按钮 */}
      <div className="text-right">
        <button
          onClick={handleSubmit}
          className="bg-primary hover:bg-red-700 text-white font-semibold font-outfit py-2 px-16 rounded "
        >
          CONFIRM ORDER
        </button>
      </div>
    </div>
  );
}
