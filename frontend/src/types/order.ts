export type OrderStatus = "CREATED" | "ACCEPTED" | "SHIPPED" | "COMPLETED" | "CANCELED";

export type OrderItem = {
  dishId: string;
  dishName: string;
  dishPrice: number; // ✅ 正确字段名
  quantity: number;
  restaurantId: string;
  subtotal?: number;
};

export type Order = {
  id: string;
  createdAt: string;
  status: OrderStatus; // ✅ 用联合类型限制 status
  recipient: string;
  phone: string;
  address: string;
  totalPrice: number;
  restaurantName: string;
  items: OrderItem[];
};
