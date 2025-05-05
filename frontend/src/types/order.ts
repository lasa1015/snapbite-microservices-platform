// src/types/order.ts

export type OrderItem = {
  dishName: string;
  quantity: number;
  price: number;
};

export type Order = {
  id: string;
  createdAt: string;
  status: string;
  recipient: string;
  phone: string;
  address: string;
  totalPrice: number;
  items: OrderItem[];
};
