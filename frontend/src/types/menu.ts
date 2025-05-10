export type Dish = {
  id: number;
  name: string;
  description: string;
  dishPrice: number;
};

export type Menu = {
  restaurantId: number;
  dishes: Dish[];
};
