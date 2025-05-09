// src/pages/MenuPage.tsx

import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import { Restaurant } from "../types/restaurant";
import { Menu } from "../types/menu";
import { useCartStore } from "../stores/cartStore";
import DishCard from "../components/DishCard";
import RestaurantMap from "../components/RestaurantMap";
import { useToast } from "../components/Toast";

const LOCAL_KEY = "guest_cart";

const MenuPage = () => {
  const { restaurantId } = useParams();
  const navigate = useNavigate();
  const { reloadFlag, triggerReload } = useCartStore();
  const showToast = useToast(); // ✅ 使用 Toast
  const [menu, setMenu] = useState<Menu | null>(null);
  const [restaurant, setRestaurant] = useState<Restaurant | null>(null);

  const restId = restaurantId ? parseInt(restaurantId) : null;
  const token = localStorage.getItem("token");

  const loadMenu = async (id: number) => {
    try {
      const res = await axios.get<Menu>(`/api/menu/restaurant/${id}`);
      setMenu(res.data);
      console.log("✅ 获取到的菜单数据：", res.data);
    } catch (err) {
      console.error("❌ 获取菜单失败", err);
    }
  };

  const loadRestaurant = async (id: number) => {
    try {
      const res = await axios.post<Restaurant>(`/api/restaurants/${id}`);
      setRestaurant(res.data);
      console.log("✅ 获取到的餐厅数据：", res.data);
    } catch (err) {
      console.error("❌ 获取餐厅信息失败", err);
    }
  };

  useEffect(() => {
    if (!restId) return;
    loadMenu(restId);
    loadRestaurant(restId);
  }, [restId]);

  const addToCart = async (dishId: string) => {
    if (!restId || !menu) return;

    const dish = menu.dishes.find((d) => d.id === dishId);
    if (!dish) return;

    console.log("🎯 加入购物车的菜品：", dish); // ✅ 打印整个菜品对象
    console.log("🎯 菜品价格：", dish.price);  // ✅ 明确输出价格

    if (token) {
      try {
        await axios.post(
          "/api/cart/add",
          {
            dishId,
            restaurantId: restId.toString(),
            restaurantName: restaurant?.name || "",
            dishName: dish.name,
            dishPrice: dish.price,
            quantity: 1,
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        showToast("Added to cart");
        console.log("✅ 已加入购物车：", dish.name);
        triggerReload();
      } catch (err: any) {
        showToast("Failed to add item");
      }
    } else {
      const local = localStorage.getItem(LOCAL_KEY);
      let cart = local ? JSON.parse(local) : [];

      // 检查是否已经存在相同的菜品
      const existing = cart.find((i: any) =>
        i.dishId === dishId && i.restaurantId === restId.toString()
      );




      if (existing) {
        existing.quantity += 1;
      } else {
        cart.push({
          id: crypto.randomUUID(),
          dishId,
          restaurantId: restId.toString(),
          restaurantName: restaurant?.name || "",
          dishName: dish.name,
          dishPrice: dish.price,
          quantity: 1,
        });
      }
      localStorage.setItem(LOCAL_KEY, JSON.stringify(cart));

      console.log("📦 当前购物车内容：", cart);  // ✅ 打印购物车内容
      showToast("Added to cart (guest)");

      triggerReload();
    }
  };

  if (!menu || !restaurant) return <p>加载中...</p>;

  return (
    <div className="max-w-screen-xl mx-auto py-4 px-14 space-y-6">
      <button
        onClick={() => navigate(-1)}
        className="text-2xl text-black mb-0 font-outfit"
      >
        ← back
      </button>

      {/* 顶部：餐厅信息 */}
      <div className="max-w-screen-xl mx-auto">
        <img
          src={`/images/restaurant_images/${restaurant.id}.jpg`}
          alt={restaurant.name}
          className="w-full max-h-[200px] object-cover rounded-lg"
        />
        <h1 className="text-4xl font-[550] font-outfit mt-6 mb-2">
          {restaurant.name}
        </h1>
        <p className="text-gray-700 text-[15px]">{restaurant.displayAddress}</p>
        <p className="text-gray-600 text-[15px]">
          ★ {restaurant.rating} / 5 · {restaurant.price} · {restaurant.category}
        </p>
      </div>

      {/* 主区域：菜单 + 地图 */}
      <div className="max-w-screen-xl mx-auto grid grid-cols-1 md:grid-cols-3 gap-8 pt-2">
        <div className="md:col-span-2">
          {menu.dishes.map((dish) => (
            <DishCard
              key={dish.id}
              dish={dish}
              restaurantName={restaurant.name}
              restaurantId={restaurant.id}
              onAdd={() => addToCart(dish.id)}
            />
          ))}
        </div>
        <div className="md:col-span-1">
          <RestaurantMap lat={restaurant.latitude} lng={restaurant.longitude} />
        </div>
      </div>
    </div>
  );
};

export default MenuPage;
