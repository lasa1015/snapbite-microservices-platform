import { CartGroup } from "../types/cart"; // ✅ 确保导入


export function useCartSync() {
  const LOCAL_KEY = "guest_cart";

  const syncCart = async (token: string) => {
    const raw = localStorage.getItem(LOCAL_KEY);
    if (!raw) return;

const groupedCart: CartGroup[] = JSON.parse(raw);

    for (const group of groupedCart) {
      for (const item of group.items) {
        //  过滤无效数据
        if (!item.dishId || !item.restaurantId || !item.quantity) continue;

        await fetch("/api/cart/add", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
         dishId: item.dishId,
            restaurantId: group.restaurantId,
            restaurantName: group.restaurantName,
            dishName: item.dishName,
            dishPrice: item.dishPrice, 
            quantity: item.quantity,
          }),
        });
      }
    }

    // 清理 guest_cart，避免重复同步
    localStorage.removeItem(LOCAL_KEY);
  };

  return { syncCart };
}
