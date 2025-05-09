// src/types/order.ts

// src/types/order.ts

export type OrderItem = {
  dishId: string;           // ✅ 由 number 改为 string，和后端匹配
  dishName: string;
  price: number;
  quantity: number;
  restaurantId: string;     // ✅ 添加或确保存在
  subtotal?: number;        // ✅ 可选：从后端接受（如果你希望前端自己算，也可不加）
};

export type Order = {
  id: string;
  createdAt: string;
  status: string;
  recipient: string;
  phone: string;
  address: string;
  totalPrice: number;
  restaurantName: string; // ✅ 加上这个字段
  items: OrderItem[];
};
