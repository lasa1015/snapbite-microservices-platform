import { useNavigate } from 'react-router-dom';

type Props = {
  restaurant: {
    id: number;
    name: string;
    imgUrl: string;
    displayAddress?: string;
    rating: number;
    reviewCount: number;
    price?: string;
    description?: string;
  };
};

const RestaurantCard: React.FC<Props> = ({ restaurant }) => {
  const navigate = useNavigate();

  return (
    <div style={styles.card}>
      <img src={restaurant.imgUrl} alt={restaurant.name} style={styles.image} />
      <div style={styles.info}>
        <h2>{restaurant.name}</h2>
        <p>{restaurant.displayAddress}</p>
        <p>⭐ {restaurant.rating} / 5 ({restaurant.reviewCount} reviews)</p>
        {restaurant.price && <p>💰 {restaurant.price}</p>}
        {restaurant.description && (
          <p style={{ fontStyle: "italic", color: "#555" }}>{restaurant.description}</p>
        )}
        <button onClick={() => navigate(`/restaurant/${restaurant.id}/menu`, {
          state: { name: restaurant.name }
        })}>
          查看详情
        </button>
      </div>
    </div>
  );
};

const styles = {
  card: {
    display: "flex",
    gap: "1rem",
    padding: "1rem",
    marginBottom: "1rem",
    border: "1px solid #ccc",
    borderRadius: "10px",
    boxShadow: "0 2px 5px rgba(0,0,0,0.1)",
    backgroundColor: "#fff",
  },
  image: {
    width: "120px",
    height: "120px",
    objectFit: "cover",
    borderRadius: "8px",
  },
  info: {
    display: "flex",
    flexDirection: "column",
    justifyContent: "space-between",
  },
};

export default RestaurantCard;
