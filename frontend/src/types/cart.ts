// src/types/cart.ts

export type CartItem = {
  id: string;
  dishId: string;
  dishName: string;
  dishPrice: number; // ✅ 确保是 dishPrice，不是 price
  restaurantId: string;
  restaurantName: string;
  quantity: number;
};
