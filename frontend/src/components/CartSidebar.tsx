import { useCartData } from "../hooks/useCartData";
import { useUserStore } from "../stores/userStore";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import AuthModal from "./AuthModal";
import { CartItem } from "../types/cart";
import { useCartStore } from "../stores/cartStore";
import { Trash2 } from "lucide-react";

export default function CartSidebar() {
  const { closeCart } = useCartStore();
  const { reloadFlag, triggerReload } = useCartStore();
  const { username, setUsername } = useUserStore();
  const { cart, loading, updateQuantity, deleteItem  } = useCartData(reloadFlag);
  const [authMode, setAuthMode] = useState<"login" | "register" | null>(null);
  const navigate = useNavigate();

  console.log("购物车原始数据 cart =", cart);

  const LOCAL_KEY = "guest_cart";

  const syncCart = async () => {
    const token = localStorage.getItem("token");
    if (!token) return;
    const local = localStorage.getItem(LOCAL_KEY);
    if (!local) return;
    const guestItems: CartItem[] = JSON.parse(local);
    for (const item of guestItems) {
      await fetch("/api/cart/add", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          dishId: item.dishId,
          restaurantId: item.restaurantId,
          quantity: item.quantity,
        }),
      });
    }
    localStorage.removeItem(LOCAL_KEY);
    triggerReload();
  };

  const handleCheckout = async () => {
    if (!username) {
      setAuthMode("login");
    } else {
      navigate("/checkout");
    }
  };

  const onLoginSuccess = async (username: string) => {
    setUsername(username);
    await syncCart();
    setAuthMode(null);
    navigate("/checkout");
  };

  const total = cart
    .flatMap((group) => group.items)
    .reduce(
      (sum, item) => sum + (item.dishPrice ||  0) * item.quantity,
      0
    );

  return (
    <div className="fixed top-0 right-0 w-[460px] h-screen bg-white border-l shadow-xl z-50 p-6 flex flex-col">
      {/* Header */}
      <div className="flex justify-between items-center mb-6">
        <button
          onClick={closeCart}
          className="w-10 h-10 rounded-full bg-gray-100 hover:bg-gray-300 flex items-center justify-center text-[28px]"
        >
          ×
        </button>
      </div>

      {/* Cart content */}
      <div className="flex-1 overflow-y-auto space-y-6">
        {loading ? (
          <p>Loading…</p>
        ) : cart.length === 0 ? 
        
        
     (
  <div className="text-center space-y-4 mt-10">
    <p className="text-[16px] text-gray-400 mb-14">No items in your cart</p>
    <img
      src="/images/empty.png"
      alt="Empty cart"
      className="mx-auto opacity-100 "
      onError={(e) => {
        console.error("empty.png 加载失败");
        e.currentTarget.style.display = "none";
      }}
    />
  </div>
)
        
        
        : (
          cart.map((group) => (
            <div key={group.restaurantId} className="border-b pb-4 mb-4">
              <div className="flex items-center mb-3">
                <h3 className="text-[20px] font-[600] font-outfit">
                  {group.restaurantName}
                </h3>
              </div>

              {group.items.map((item) => {
                const imagePath = `/images/dish_images/${group.restaurantId}/${item.dishId}.jpg`;

                return (
                  <div
                    key={`${item.dishId}-${group.restaurantId}`}
                    className="flex items-center mb-3"
                  >
                    <img
                      src={imagePath}
                      alt={item.dishName}
                      className="w-16 h-16 rounded-md object-cover"
                      onError={(e) => {
                        console.error("图片加载失败：", imagePath);
                        e.currentTarget.onerror = null;
                        e.currentTarget.src = "/images/placeholder.jpg";
                      }}
                    />
                    <div className="ml-3 flex-1">
                      <div className="font-[500] text-[18px] font-outfit">
                        {item.dishName || "Unknown Dish"}
                      </div>
                      <div className="text-sm text-gray-500">
                        €{(item.dishPrice || 0).toFixed(2)}
                      </div>
                    </div>
                    <div className="flex items-center space-x-2">
                      <button
                        onClick={() => updateQuantity(item.id, item.quantity - 1)}
                        className="w-8 h-8 border rounded text-lg"
                      >
                        -
                      </button>
                      <span>{item.quantity}</span>
                      <button
                        onClick={() => updateQuantity(item.id, item.quantity + 1)}
                        className="w-8 h-8 border rounded text-lg"
                      >
                        +
                      </button>
                      <button
                        onClick={() => deleteItem(item.id)}
                        className="text-gray-500 ml-2 hover:text-red-700"
                      >
                        <Trash2 className="w-5 h-5" />
                      </button>
                    </div>
                  </div>
                );
              })}
            </div>
          ))
        )}

        {/* Subtotal */}
        {cart.length > 0 && (
          <div className="space-y-2 pt-2">
            <div className="flex justify-between font-[400] text-[20px] font-outfit">
              <span>Subtotal</span>
              <span>€{total.toFixed(2)}</span>
            </div>
          </div>
        )}
      </div>

      {/* Checkout */}
      {cart.length > 0 && (
        <div className="pt-4">
          <button
            onClick={handleCheckout}
            className="w-full bg-primary hover:bg-red-700 text-white font-[500] py-2 rounded-[10px] font-outfit text-[18px]"
          >
            Go to checkout
          </button>
        </div>
      )}

      {/* Auth Modal */}
      {authMode && (
        <AuthModal
          mode={authMode}
          onClose={() => setAuthMode(null)}
          onLoginSuccess={onLoginSuccess}
          setMode={setAuthMode}
        />
      )}
    </div>
  );
}
