// src/types/restaurant.ts

export type Restaurant = {
  id: number;
  name: string;
  imgUrl: string;
  displayAddress?: string;
  rating: number;
  reviewCount: number;
  price?: string;
  category?: string;
  description?: string;
  latitude?: number;
  longitude?: number;
};
