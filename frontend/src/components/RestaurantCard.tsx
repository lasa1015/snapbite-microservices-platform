// src/components/RestaurantCard.tsx
import { useNavigate } from 'react-router-dom';
import { Restaurant } from "../types/restaurant";

// 根据评分返回标签
function getRatingLabel(rating: number) {

  
  if (rating >= 4.5) return "Excellent";
  if (rating >= 4.2) return "Good";
  if (rating >= 3.8) return "Average";
  return "Poor";
}


// 使用在types中写好的餐厅类型数据
// 只能访问该类型中声明的字段
type Props = {
  restaurant: Restaurant;
};



const RestaurantCard: React.FC<Props> = ({ restaurant }) => {


  const navigate = useNavigate();

  return (
    <div
      onClick={() => navigate(`/restaurant/${restaurant.id}/menu`, {
        state: { name: restaurant.name }
      })}
      className="cursor-pointer group relative"
    >
      {/* 图片 */}
      <div className="w-full h-[190px] overflow-hidden rounded-xl relative">
        <img
          src={restaurant.imgUrl}
          alt={restaurant.name}
          className="w-full h-full object-cover transition-transform duration-300 group-hover:scale-105"
        />

        {/* 悬浮遮罩层 */}
        <div className="absolute inset-0 bg-red-900 bg-opacity-40 opacity-0 group-hover:opacity-100 transition-opacity duration-300 flex items-center justify-center rounded-xl">
        <span className="text-white text-2xl font-light">View Details</span>

        </div>
      </div>

      {/* 文字信息区域 */}
      <div className="mt-2 space-y-0.5 px-1">
        <h2 className="text-[20px] font-[450] text-gray-900 font-outfit">{restaurant.name}</h2>

        <p className="text-sm text-gray-800">        
★ {restaurant.rating.toFixed(1)}{" "}
          <span className="text-gray-500 ml-1">{getRatingLabel(restaurant.rating)}</span>
          <span className="text-gray-400 ml-1">(500+)</span>
        </p>

      </div>
    </div>
  );
};

export default RestaurantCard;