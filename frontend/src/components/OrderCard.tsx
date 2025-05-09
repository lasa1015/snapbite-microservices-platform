import { Order } from "../types/Order";
import { useState } from "react";
import ConfirmDialog from "./ConfirmDialog";

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
    ACCEPTED: "bg-indigo-100 text-indigo-700",
    SHIPPED: "bg-amber-100 text-amber-700",
    COMPLETED: "bg-emerald-100 text-emerald-700",
    CANCELED: "bg-gray-200 text-gray-500",
  };

  const statusLabels: Record<string, string> = {
    CREATED: "Pending",
    ACCEPTED: "Accepted",
    SHIPPED: "On the Way",
    COMPLETED: "Completed",
    CANCELED: "Canceled",
  };

  const [showCancelConfirm, setShowCancelConfirm] = useState(false);
  const [showReceiptConfirm, setShowReceiptConfirm] = useState(false);

  return (
    <div className="bg-white rounded-xl shadow-sm border p-5 space-y-4 w-full max-w-[800px] mx-auto">
      <div className="flex justify-between items-center">
        <h3 className="text-[24px] font-[500] font-outfit">
          {order.restaurantName ?? "Unknown Restaurant"}
        </h3>
        <span
          className={`text-[14px] font-[500] font-outfit px-6 py-2 rounded-full ${statusStyles[order.status]}`}
        >
          {statusLabels[order.status] ?? order.status}
        </span>
      </div>

      <p className="text-sm text-gray-500">
        {new Date(order.createdAt).toLocaleString()}
      </p>

      <div className="space-y-4">
        {order.items.map((item, index) => (
          <div key={index} className="flex items-center">
            <img
              src={`/images/dish_images/${item.restaurantId}/${item.dishId}.jpg`}
              alt={item.dishName}
              className="w-24 h-24 rounded object-cover mr-3"
              onError={(e) => {
                e.currentTarget.onerror = null;
                e.currentTarget.src = "/images/placeholder.jpg";
              }}
            />
            <div className="ml-3 flex-1">
              <div className="text-[19px] text-gray-900 font-outfit">
                {item.dishName}
              </div>
              <div className="text-[14px] text-gray-500">× {item.quantity}</div>
            </div>
            <div className="text-[18px] text-gray-700 font-[400] font-outfit">
              €{item.price.toFixed(2)}
            </div>
          </div>
        ))}
      </div>

      <hr />

      <div className="flex justify-between text-gray-800 text-[24px] font-[500] font-outfit">
        <span>Subtotal</span>
        <span>€{order.totalPrice.toFixed(2)}</span>
      </div>

      <div className="space-y-1 text-[18px] font-outfit font-[300] text-gray-700 mt-2">
        <p>
          <span className="font-medium">Recipient:</span> {order.recipient}
        </p>
        <p>
          <span className="font-medium">Phone:</span> {order.phone}
        </p>
        <p>
          <span className="font-medium">Address:</span> {order.address}
        </p>
      </div>

      {(canCancel || canConfirm) && (
        <div className="flex justify-end space-x-3 pt-2">
          {canCancel && (
            <button
              onClick={() => setShowCancelConfirm(true)}
              className="bg-primary text-white px-12 py-2 rounded-md text-sm hover:bg-red-800 transition font-outfit font-[500]"
            >
              CANCEL
            </button>
          )}
          {canConfirm && (
            <button
              onClick={() => setShowReceiptConfirm(true)}
              className="bg-green-500 text-white px-10 py-1.5 rounded-md text-sm hover:bg-green-700 transition font-outfit font-[500]"
            >
              CONFIRM RECEIPT
            </button>
          )}
        </div>
      )}

      <ConfirmDialog
        open={showCancelConfirm}
        title="Cancel Order"
        description="Are you sure you want to cancel this order?"
        onConfirm={() => {
          onCancel(order.id);
          setShowCancelConfirm(false);
        }}
        onCancel={() => setShowCancelConfirm(false)}
      />

      <ConfirmDialog
        open={showReceiptConfirm}
        title="Confirm Receipt"
        description="Have you received your order?"
        onConfirm={() => {
          onConfirm(order.id);
          setShowReceiptConfirm(false);
        }}
        onCancel={() => setShowReceiptConfirm(false)}
      />
    </div>
  );
}