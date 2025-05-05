// src/types/cart.ts

export type CartItem = {
  id: string;
  restaurantId: string;
  restaurantName?: string;
  dishId: string;
  dishName?: string;
  price?: number;
  quantity: number;
};
