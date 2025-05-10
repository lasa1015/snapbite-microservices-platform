import axios from "axios";

import { CartGroup } from "../types/cart"; // 别忘了这句


export async function fetchCartFromServer(token: string): Promise<CartGroup[]> {
  const res = await axios.get("/api/cart", {
    headers: { Authorization: `Bearer ${token}` },
  });
  return res.data;
}

export async function updateItemQuantity(id: string, quantity: number, token: string) {
  await axios.put(`/api/cart/${id}/quantity`, null, {
    params: { quantity },
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
  });
}

export async function deleteCartItem(id: string, token: string) {
  await axios.delete(`/api/cart/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
}

export async function clearCartOnServer(token: string) {
  await axios.delete("/api/cart/clear", {
    headers: { Authorization: `Bearer ${token}` },
  });
}
