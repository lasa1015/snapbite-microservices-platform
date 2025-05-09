import { addToGuestCart } from "../utils/guestCartUtils";
import { useUserStore } from "../stores/userStore";
import { useToast } from "../components/Toast";


interface DishCardProps {
  dish: {
    id: number;
    name: string;
    price: number;
    description?: string;
  };
  restaurantId: number; // ✅ 传入
  restaurantName?: string; // ✅ 可选传入（推荐加上）
  onAdd: (dishId: number) => void;
}

export default function DishCard({ dish, restaurantId, restaurantName = "Unknown", onAdd }: DishCardProps) {
  const { username } = useUserStore(); // ✅ 判断是否登录
  const imagePath = `/images/dish_images/${restaurantId}/${dish.id}.jpg`;
const showToast = useToast();

const handleClick = () => {
  if (!username) {

    // ✅ 游客模式：写入 localStorage（分组结构，统一字段名）
    addToGuestCart(
      restaurantId.toString(),
      restaurantName,
      dish.id.toString(),
      dish.name,
      dish.price
    );
    showToast("Added to cart");
    return;
  }

  // 登录用户走原来的逻辑
  onAdd(dish.id);
};


  return (
    <div className="flex rounded-[4px] overflow-hidden border border-gray-200 bg-white mb-5 min-h-[120px]">
      {/* ✅ 左图 */}
      <div className="w-[200px] h-[150px] bg-gray-100 flex-shrink-0">
        <img
          src={imagePath}
          alt={dish.name}
          className="w-full h-full object-cover"
          onError={(e) => {
            (e.target as HTMLImageElement).src = "/images/placeholder.jpg";
          }}
        />
      </div>

      {/* ✅ 内容区域 */}
      <div className="flex-1 p-4 flex flex-col justify-between relative">
        <div>
          <h3 className="text-2xl font-semibold text-gray-900 font-outfit font-[550]">{dish.name}</h3>
          <p className="text-gray-500 text-sm line-clamp-2 mt-1">
            {dish.description || "No description available."}
          </p>
        </div>

        {/* ✅ 底部价格 + 添加按钮 */}
        <div className="flex justify-between items-end mt-4">
          <p className="text-gray-900 text-[18px] font-outfit font-[300]">€{dish.price.toFixed(2)}</p>
          <button
            onClick={handleClick}
            className="w-10 h-10 rounded-full bg-primary text-white flex items-center justify-center text-2xl hover:bg-red-700"
          >
            +
          </button>
        </div>
      </div>
    </div>
  );
}
