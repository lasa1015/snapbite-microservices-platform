import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { Restaurant } from "../types/restaurant";
import { Menu } from "../types/menu";
import { useCartStore } from "../stores/cartStore";
import DishCard from "../components/DishCard";
import RestaurantMap from "../components/RestaurantMap";

const LOCAL_KEY = "guest_cart";

const MenuPage = () => {
  const { restaurantId } = useParams();
  const navigate = useNavigate();
  const { reloadFlag, triggerReload } = useCartStore();
  const [menu, setMenu] = useState<Menu | null>(null);
  const [restaurant, setRestaurant] = useState<Restaurant | null>(null);

  const restId = restaurantId ? parseInt(restaurantId) : null;
  const token = localStorage.getItem("token");

  useEffect(() => {
    if (!restId) return;
    fetch(`/api/menu/restaurant/${restId}`).then(res => res.json()).then(setMenu);
    fetch(`/api/restaurants/${restId}`, { method: "POST" }).then(res => res.json()).then(setRestaurant);
  }, [restId]);



  const addToCart = async (dishId: number) => {
    if (!restId || !menu) return;
    const dish = menu.dishes.find(d => d.id === dishId);
    if (!dish) return;

    if (token) {
      const res = await fetch("/api/cart/add", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          dishId: dishId.toString(),
          restaurantId: restId.toString(),
          quantity: 1
        }),
      });
      if (res.ok) {
        alert("✅ 已加入购物车");
        triggerReload();
      } else {
        alert("❌ 加入失败：" + await res.text());
      }
    } else {
      const local = localStorage.getItem(LOCAL_KEY);
      let cart = local ? JSON.parse(local) : [];
      const existing = cart.find(i => i.dishId === dishId.toString());
      if (existing) existing.quantity += 1;
      else 
      cart.push({ id: crypto.randomUUID(), dishId: dishId.toString(), restaurantId: restId.toString(), dishName: dish.name, price: dish.price, quantity: 1 });
    
      localStorage.setItem(LOCAL_KEY, JSON.stringify(cart));
      alert("✅ 已加入购物车（未登录）");
      triggerReload();
    }
  };

  if (!menu || !restaurant) return <p>加载中...</p>;

  return (
    <div className="max-w-screen-xl mx-auto p-6 space-y-6">
      <button onClick={() => navigate(-1)} className="text-2xl text-red-600 mb-0 font-outfit">← back</button>

      {/* 顶部：餐厅信息 */}
      <div >
        
        <img src={restaurant.imgUrl} alt={restaurant.name} className="w-full max-h-[200px] object-cover rounded-lg" />
        
        
        <h1 className="text-4xl font-[550]  font-outfit mt-6 mb-2">{restaurant.name}</h1>

        <p className="text-gray-700 text-[15px]">{restaurant.displayAddress}</p>

        <p className="text-gray-600 text-[15px]">★ {restaurant.rating} / 5 · {restaurant.price} · {restaurant.category}</p>
        
        {/* {restaurant.description && <p className="italic text-gray-500">{restaurant.description}</p>} */}
      </div>

      {/* 主区域：左菜单 / 右地图 */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-8 pt-2">

      <div className="md:col-span-2 min-w-[730px] mx-auto">

  {menu.dishes.map(dish => (
      <DishCard key={dish.id} dish={dish} restaurantId={restaurant.id} onAdd={addToCart} />
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
