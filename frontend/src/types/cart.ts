// src/types/cart.ts

export type CartItem = {
  id: string;
  dishId: string;
  dishName: string;
  dishPrice: number;
  restaurantId: string;
  restaurantName: string;
  quantity: number;
};

export type CartGroup = {
  restaurantId: string;
  restaurantName: string;
  items: CartItem[];
};
