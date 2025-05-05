import React from "react";

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

type Props = {
  order: Order;
  onCancel: (id: string) => void;
};

export default function OrderCard({ order, onCancel }: Props) {
  const canCancel = order.status === "CREATED";

  const handleCancel = () => {
    if (window.confirm("确定要取消该订单吗？")) {
      onCancel(order.id);
    }
  };

  const statusColor = {
    CREATED: "#007bff",
    SHIPPED: "#17a2b8",
    COMPLETED: "#28a745",
    CANCELLED: "#dc3545",
  }[order.status] || "#6c757d";

  return (
    <div style={{
      border: "1px solid #ccc",
      borderRadius: "8px",
      padding: "1rem",
      marginBottom: "1rem",
      backgroundColor: "#fff"
    }}>
      <p><strong>订单号：</strong>{order.id.slice(0, 8)}...</p>
      <p><strong>下单时间：</strong>{new Date(order.createdAt).toLocaleString()}</p>
      <p>
        <strong>状态：</strong>
        <span style={{ color: statusColor, fontWeight: "bold" }}>{order.status}</span>
      </p>
      <p><strong>收件人：</strong>{order.recipient}（{order.phone}）</p>
      <p><strong>地址：</strong>{order.address}</p>
      <p><strong>总价：</strong>€{order.totalPrice.toFixed(2)}</p>

      <ul>
        {order.items.map((item, index) => (
          <li key={index}>
            🍽️ {item.dishName} × {item.quantity} - €{item.price.toFixed(2)}
          </li>
        ))}
      </ul>

      {canCancel && (
        <button
          onClick={handleCancel}
          style={{
            marginTop: "1rem",
            backgroundColor: "#dc3545",
            color: "#fff",
            padding: "6px 12px",
            border: "none",
            borderRadius: "5px",
            cursor: "pointer"
          }}
        >
          ❌ 取消订单
        </button>
      )}
    </div>
  );
}
