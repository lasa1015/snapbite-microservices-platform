export type Dish = {
  id: string;
  name: string;
  description: string;
  price: number;
};

export type Menu = {
  restaurantId: number;
  dishes: Dish[];
};
