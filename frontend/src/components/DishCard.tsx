interface DishCardProps {
  dish: {
    id: number;
    name: string;
    price: number;
    description?: string;
  };
  restaurantId: number;  // ✅ 新增参数：来自 MenuPage
  onAdd: (dishId: number) => void;
}

export default function DishCard({ dish, restaurantId, onAdd }: DishCardProps) {
  const imagePath = `/images/dish_images/${restaurantId}/${dish.id}.jpg`;

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
            onClick={() => onAdd(dish.id)}
            className="w-10 h-10 rounded-full bg-primary text-white flex items-center justify-center text-2xl hover:bg-red-700"
          >
            +
          </button>
        </div>
      </div>
    </div>
  );
}
