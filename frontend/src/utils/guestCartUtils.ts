// utils/guestCartUtils.ts

import { v4 as uuidv4 } from "uuid";

const LOCAL_KEY = "guest_cart";

export function addToGuestCart(
  restaurantId: string,
  restaurantName: string,
  dishId: string,
  dishName: string,
  dishPrice: number
) {
  const raw = localStorage.getItem(LOCAL_KEY);
  const cart = raw ? JSON.parse(raw) : [];

  // 查找该餐厅是否已存在
  const existingGroup = cart.find((g: any) => g.restaurantId === restaurantId);

  if (existingGroup) {
    // 查找是否已存在该菜品
    const existingItem = existingGroup.items.find((item: any) => item.dishId === dishId);

    if (existingItem) {
      existingItem.quantity += 1;
    } else {
      existingGroup.items.push({
        id: uuidv4(),
        dishId,
        dishName,
        dishPrice,
        restaurantId,
        restaurantName,
        quantity: 1,
      });
    }
  } else {
    cart.push({
      restaurantId,
      restaurantName,
      items: [
        {
          id: uuidv4(),
          dishId,
          dishName,
          dishPrice,
          restaurantId,
          restaurantName,
          quantity: 1,
        },
      ],
    });
  }

  localStorage.setItem(LOCAL_KEY, JSON.stringify(cart));
}
