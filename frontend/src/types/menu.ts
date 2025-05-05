export type Dish = {
  id: number;
  name: string;
  description: string;
  price: number;
};

export type Menu = {
  restaurantId: number;
  dishes: Dish[];
};
