import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useUserStore } from "../stores/userStore";
import { useCartStore } from "../stores/cartStore";
import AuthModal from "./AuthModal";
import { ShoppingCart, User } from "lucide-react";
import { useRef, useEffect } from "react";
import { Role } from "../types/user"; 



export default function UserStatusBar() {
  const { openCart } = useCartStore();
  const { username, role, setUsername, setRole } = useUserStore();
  const [authMode, setAuthMode] = useState<"login" | "register" | null>(null);
  const [showDropdown, setShowDropdown] = useState(false);
  const navigate = useNavigate();
  const dropdownRef = useRef<HTMLDivElement>(null);

  const handleLogout = () => {
    localStorage.clear();
    setUsername(null);
    setRole(null);
    navigate("/");
  };


  useEffect(() => {
  function handleClickOutside(event: MouseEvent) {
    if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
      setShowDropdown(false);
    }
  }

  document.addEventListener("mousedown", handleClickOutside);
  return () => {
    document.removeEventListener("mousedown", handleClickOutside);
  };
}, []);


  return (
    <div className="px-28 py-1 flex justify-between items-center shadow-sm relative bg-primary text-white sticky top-0 z-50 border-rounded-b-[8px]"
     style={{
        // borderRadius: "16px",
        // margin: "0px 70px",

      }}
    
    
    >
   
    
    
      
      {/* Logo */}
      <img
        src="/images/logo3.png"
        alt="App Logo"
        onClick={() => navigate("/")}
        className="h-12 cursor-pointer object-contain"
      />

      {/* 右上角区域 */}
      <div className="flex items-center space-x-4">
        
        {username ? (


          // 登录状态下的用户菜单
          <div className="relative">
            <button
              onClick={() => setShowDropdown((prev) => !prev)}
              className="bg-gray-100 text-gray-800 px-4 py-1.5 rounded-full flex items-center space-x-2 hover:bg-gray-200 transition  text-primary text-[800] font-outfit"
            >
              <User className="w-4 h-4" />
              <span>{username}</span>
            </button>

            {showDropdown && (
          <div  ref={dropdownRef} className="absolute right-0 mt-2 bg-white rounded-[8px] py-2 z-50 
          border border-gray-200 drop-shadow-[0_1px_2px_rgba(0,0,0,0.15)]">

                <button
                  onClick={() => {
                    navigate(role === "MERCHANT" ? "/merchant" : "/my-orders");
                    setShowDropdown(false);
                  }}
                  className="block w-full text-left px-4 py-2 hover:bg-gray-100 text-sm whitespace-nowrap text-gray-800"
                >
                  {role === "MERCHANT" ? "Shop Orders" : "My Orders"}
                </button>
                <button
                  onClick={handleLogout}
                  className="block w-full text-left px-4 py-2 hover:bg-gray-100 text-sm text-gray-800 whitespace-nowrap"
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
          className="bg-white  px-4 py-1.5 rounded-full flex items-center space-x-2 hover:bg-gray-200 transition text-primary text-[800] font-outfit "
        >
          <User className="w-4 h-4" />
          <span>Login</span>
        </button>
        )}

        {/* 所有人都能看到购物车按钮（除了商户） */}
        {role !== "MERCHANT" && (
          <button
            onClick={openCart}
            className="bg-white text-primary rounded-full py-2 px-4 hover:bg-gray-300 transition"
          >
            <ShoppingCart className="w-5 h-5" />
          </button>
        )}

      </div>

      {/* 登录/注册弹窗 */}
     {authMode && (
  <AuthModal
    mode={authMode}
    setMode={setAuthMode}
    onClose={() => setAuthMode(null)}


   onLoginSuccess={(name: string, role: Role) => {
  setUsername(name);
  setRole(role);
  setAuthMode(null);

  if (role === "MERCHANT") {
    navigate("/merchant");
  } else {
    // 只有普通用户才同步 guest_cart 数据
    const localCart = JSON.parse(localStorage.getItem("guest_cart") || "[]");

    if (Array.isArray(localCart)) {
useCartStore.getState().setCartItems(localCart);
    }

    navigate("/"); // ✅ 可选跳转回主页
  }
}}


   
  />
)}

    </div>
  );
}
