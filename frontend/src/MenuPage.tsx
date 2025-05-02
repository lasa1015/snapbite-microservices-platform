import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';

type Dish = {
  id: string;
  name: string;
  description: string;
  price: number;
};

type Menu = {
  restaurantId: number;
  restaurantName: string;
  items: Dish[];
};

const MenuPage = () => {
  const { restaurantId } = useParams();
  const [menu, setMenu] = useState<Menu | null>(null);

  useEffect(() => {
    fetch(`/api/menu/restaurant/${restaurantId}`)
      .then((res) => res.json())
      .then((data) => setMenu(data))
      .catch((err) => console.error("获取菜单失败：", err));
  }, [restaurantId]);

  if (!menu) return <p>加载中...</p>;

  return (
    <div style={{ padding: "2rem" }}>
      <h1>🍛 {menu.restaurantName} 的菜单</h1>
      <ul>
        {menu.items.map(dish => (
          <li key={dish.id}>
            <strong>{dish.name}</strong> - €{dish.price.toFixed(2)} <br />
            <em>{dish.description}</em>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default MenuPage;
