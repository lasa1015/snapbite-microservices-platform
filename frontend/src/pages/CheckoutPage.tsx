// src/pages/CheckoutPage.tsx
import { useCartStore } from "../stores/cartStore";
import { useCartData } from "../hooks/useCartData";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export default function CheckoutPage() {


  const { reloadFlag } = useCartStore();



  const { cart } = useCartData(reloadFlag);
  const navigate = useNavigate();

  const [recipient, setRecipient] = useState("");
  const [phone, setPhone] = useState("");
  const [address, setAddress] = useState("");

  const total = cart.reduce((s, c) => s + (c.price ?? 0) * c.quantity, 0);

  const handleSubmit = async () => {
    if (!address || !recipient || !phone) {
      alert("请填写完整的收件信息");
      return;
    }

    const token = localStorage.getItem("token");
    if (!token) return alert("未登录");

    const res = await fetch("/api/order/checkout", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ address, recipient, phone })

    });

    if (res.ok) {
      alert("✅ 下单成功！");
      navigate("/");
    } else {
      alert("❌ 下单失败：" + await res.text());
    }
  };

  return (
    <div style={{ padding: "2rem" }}>
      <h2>🧾 确认订单</h2>

      {cart.map(item => (
        <div key={item.id}>
          <p>
            🍽️ {item.dishName} × {item.quantity} — €{(item.price ?? 0).toFixed(2)}
          </p>
        </div>
      ))}

      <hr />
      <p><strong>总计：</strong> €{total.toFixed(2)}</p>

      <h3>📮 收货信息</h3>
      <input value={recipient} onChange={e => setRecipient(e.target.value)} placeholder="收件人姓名" /><br />
      <input value={phone} onChange={e => setPhone(e.target.value)} placeholder="联系电话" /><br />
      <input value={address} onChange={e => setAddress(e.target.value)} placeholder="详细地址" /><br />

      <button onClick={handleSubmit} style={{ marginTop: "1rem", background: "#007bff", color: "#fff" }}>
        ✅ 提交订单
      </button>
    </div>
  );
}
