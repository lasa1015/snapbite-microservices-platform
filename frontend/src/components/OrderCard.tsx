import { Order } from "../types/Order";
import { CheckCircle, XCircle } from "lucide-react";

type Props = {
  order: Order;
  onCancel: (id: string) => void;
  onConfirm: (id: string) => void;
};

export default function OrderCard({ order, onCancel, onConfirm }: Props) {
  const canCancel = order.status === "CREATED";
  const canConfirm = order.status === "SHIPPED";

  const statusStyles = {
    CREATED: "bg-blue-100 text-blue-700",
    SHIPPED: "bg-cyan-100 text-cyan-700",
    COMPLETED: "bg-green-100 text-green-700",
    CANCELED: "bg-gray-200 text-gray-500",
  };

  return (
    <div className="bg-white rounded-xl shadow-sm border p-5 space-y-4 w-full max-w-[800px] mx-auto">
      
      {/* 顶部：餐厅 + 状态标签 */}
      <div className="flex justify-between items-center">
        <h3 className="text-lg font-semibold">{order.restaurantName ?? "Unknown Restaurant"}</h3>
        <span className={`text-xs font-semibold px-2 py-1 rounded-full ${statusStyles[order.status]}`}>
          {order.status}
        </span>
      </div>

      {/* 时间 */}
      <p className="text-sm text-gray-500">
        {new Date(order.createdAt).toLocaleString()}
      </p>

      {/* 餐品列表 */}
      <div className="space-y-6">
        {order.items.map((item, index) => (
          <div key={index} className="flex items-center">
            <img
              src={`/images/dish_images/${item.restaurantId}/${item.dishId}.jpg`}
              alt={item.dishName}
              className="w-16 h-16 rounded object-cover mr-3"
              onError={(e) => (e.currentTarget.style.display = "none")} // 如果图片加载失败就隐藏
            />
            <div className="ml-3 flex-1">
              <div className="font-medium">{item.dishName}</div>
              <div className="text-sm text-gray-500">x {item.quantity}</div>
            </div>
            <div className="text-sm text-gray-600">
              €{item.price.toFixed(2)}
            </div>
          </div>
        ))}
      </div>

      <hr />

{/* 小计（单独一块） */}
<div className="flex justify-between font-outfit font-semibold text-[20px] text-gray-800">
  <span>Subtotal</span>
  <span>€{order.totalPrice.toFixed(2)}</span>
</div>

{/* 收件人信息区域 */}
<div className="space-y-1 text-sm text-gray-700 mt-2">
  <p><strong>Recipient:</strong> {order.recipient}</p>
  <p><strong>Phone:</strong> {order.phone}</p>
  <p><strong>Address:</strong> {order.address}</p>
</div>



 {/* 操作按钮区域，仅 CREATED / SHIPPED 状态下显示操作按钮 */}
{(canCancel || canConfirm) && (
  <div className="flex justify-end space-x-3 pt-2">
    {canCancel && (
      <button
        onClick={() => {
          if (window.confirm("确定要取消该订单吗？")) onCancel(order.id);
        }}
        className="bg-red-700 text-white px-4 py-1.5 rounded-md text-sm hover:bg-red-800 transition 
        font-outfit  font-[500] text-[16px]"
      >
        CANCEL
      </button>
    )}

    {canConfirm && (
      <button
        onClick={() => {
          if (window.confirm("确认已经收到订单？")) onConfirm(order.id);
        }}
        className="bg-green-600 text-white px-4 py-1.5 rounded-md text-sm hover:bg-green-700 transition"
      >
        CONFIRM RECEIPT
      </button>
    )}
  </div>
)}

    </div>
  );
}
