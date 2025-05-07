import { useCartData } from "../hooks/useCartData";
import { useCartStore } from "../stores/cartStore";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export default function CheckoutPage() {
  const { reloadFlag } = useCartStore();
  const { cart } = useCartData(reloadFlag);
  const navigate = useNavigate();

  const [recipient, setRecipient] = useState("");
  const [phone, setPhone] = useState("");
  const [address, setAddress] = useState("");

  const grouped = cart.reduce((acc, c) => {
    const key = c.restaurantId;
    (acc[key] ||= {
      name: c.restaurantName ?? `Restaurant ${key}`,
      restaurantId: c.restaurantId,
      items: [],
    });
    acc[key].items.push(c);
    return acc;
  }, {} as Record<string, { name: string; restaurantId: number; items: typeof cart }[number][]>);

  const total = cart.reduce((s, c) => s + (c.price ?? 0) * c.quantity, 0);

  const handleSubmit = async () => {
    if (!address || !recipient || !phone) {
      alert("Please complete all delivery details");
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
      alert("✅ Order placed successfully!");
      navigate("/");
    } else {
      alert("❌ Order failed: " + (await res.text()));
    }
  };

  return (
    <div className="mx-auto px-20 py-10" style={{ minWidth: "900px" }}>


      {Object.values(grouped).map((group) => (
        <div key={group.restaurantId} className="mb-6 border-b pb-4">
          <h3 className="text-[24px] font-semibold mb-2 font-outfit">{group.name}</h3>
          {group.items.map((item) => (
            <div key={item.id} className="flex items-center justify-between mb-2">
              <div className="flex items-center">
                <img
                  src={`/images/dish_images/${item.restaurantId}/${item.dishId}.jpg`}
                  alt={item.dishName}
                  className="w-12 h-12 rounded-md object-cover mr-3"
                />
                <div>
                  <div className="text-[20px] font-outfit ">{item.dishName}</div>
                  <div className="text-m text-gray-500">× {item.quantity}</div>
                </div>
              </div>
              <div className="text-right font-medium">
                €{((item.price ?? 0) * item.quantity).toFixed(2)}
              </div>
            </div>
          ))}
        </div>
      ))}

      <div className="flex justify-between  pb-4 font-[400] text-[24px] mb-6 font-outfit">
        <span>Subtotal</span>
        <span>€{total.toFixed(2)}</span>
      </div>

      <div className="space-y-4 mb-6">
        <h3 className=" font-semibold font-outfit text-[24px]">Delivery details</h3>
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

      <div className="text-right">
        <button
          onClick={handleSubmit}
          className="bg-primary hover:bg-red-700 text-white font-semibold font-outfit py-2 px-10 rounded"
        >
          CONFIRM ORDER
        </button>
      </div>
    </div>
  );
}
