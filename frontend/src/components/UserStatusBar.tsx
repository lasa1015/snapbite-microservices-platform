import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useUserStore } from "../stores/userStore";
import { useCartStore } from "../stores/cartStore";
import AuthModal from "./AuthModal";
import { ShoppingCart, User } from "lucide-react";

export default function UserStatusBar() {
  const { openCart } = useCartStore();
  const { username, role, setUsername, setRole } = useUserStore();
  const [authMode, setAuthMode] = useState<"login" | "register" | null>(null);
  const [showDropdown, setShowDropdown] = useState(false);
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.clear();
    setUsername(null);
    setRole(null);
    navigate("/");
  };

  return (
    <div className="px-20 py-3 flex justify-between items-center shadow-sm relative">
      
      {/* Logo */}
      <img
        src="/images/logo2.png"
        alt="App Logo"
        onClick={() => navigate("/")}
        className="h-10 cursor-pointer object-contain"
      />

      {/* 右上角区域 */}
      <div className="flex items-center space-x-4">
        
        {username ? (
          // 登录状态下的用户菜单
          <div className="relative">
            <button
              onClick={() => setShowDropdown((prev) => !prev)}
              className="bg-gray-100 text-gray-800 px-4 py-1.5 rounded-full flex items-center space-x-2 hover:bg-gray-200 transition text-sm"
            >
              <User className="w-4 h-4" />
              <span>{username}</span>
            </button>

            {showDropdown && (
          <div className="absolute right-0 mt-2 bg-white rounded-[8px] py-2 z-50 
          border border-gray-200 drop-shadow-[0_1px_2px_rgba(0,0,0,0.15)]">

                <button
                  onClick={() => {
                    navigate(role === "MERCHANT" ? "/merchant" : "/my-orders");
                    setShowDropdown(false);
                  }}
                  className="block w-full text-left px-4 py-2 hover:bg-gray-100 text-sm whitespace-nowrap"
                >
                  {role === "MERCHANT" ? "Shop Orders" : "My Orders"}
                </button>
                <button
                  onClick={handleLogout}
                  className="block w-full text-left px-4 py-2 hover:bg-gray-100 text-sm"
                >
                  Logout
                </button>
              </div>
            )}
          </div>
        ) : (
          // 未登录显示 Login 按钮
          <button
          onClick={() => setAuthMode("login")}
          className="bg-gray-100 text-gray-800 px-4 py-1.5 rounded-full flex items-center space-x-2 hover:bg-gray-200 transition text-sm"
        >
          <User className="w-4 h-4" />
          <span>Login</span>
        </button>
        )}

        {/* 所有人都能看到购物车按钮 */}
        <button
          onClick={openCart}
          className="bg-red-700 text-white rounded-full p-1.5 hover:bg-red-800 transition"
        >
          <ShoppingCart className="w-5 h-5" />
        </button>
      </div>

      {/* 登录/注册弹窗 */}
      {authMode && (
        <AuthModal
          mode={authMode}
          setMode={setAuthMode}
          onClose={() => setAuthMode(null)}
          onLoginSuccess={(name) => {
            setUsername(name);
            setAuthMode(null);

            // ✅ 可选：同步购物车（如果你需要）
            const localCart = JSON.parse(localStorage.getItem("cart") || "[]");
            if (Array.isArray(localCart)) {
              useCartStore.getState().setCartItems(localCart);
            }
          }}
        />
      )}
    </div>
  );
}
