// src/components/MerchantOrderCard.tsx
type Props = {
  order: any;
  onShip: (id: string) => void;
  onCancel: (id: string) => void;
};

export default function MerchantOrderCard({ order, onShip, onCancel }: Props) {
  return (
    <div style={{ border: "1px solid #ccc", padding: "1rem", marginBottom: "1rem" }}>
      <p>订单号：{order.id}</p>
      <p>收件人：{order.recipient}</p>
      <p>地址：{order.address}</p>
      <p>电话：{order.phone}</p>
      <p>创建时间：{new Date(order.createdAt).toLocaleString()}</p>
      <p>总金额：¥{order.totalPrice}</p>
      <p>状态：{order.status}</p>
      <ul>
        {order.items.map((item: any, i: number) => (
          <li key={i}>{item.dishName} × {item.quantity}（¥{item.price}）</li>
        ))}
      </ul>

      {order.status === "CREATED" && (
        <>
          <button onClick={() => onShip(order.id)}>发货</button>
          <button onClick={() => onCancel(order.id)} style={{ marginLeft: "0.5rem" }}>取消订单</button>
        </>
      )}
    </div>
  );
}
