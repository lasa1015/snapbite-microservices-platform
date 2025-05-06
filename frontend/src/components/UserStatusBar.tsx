import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useUserStore } from "../stores/userStore"; //  使用 Zustand 管理用户状态
import AuthModal from "./AuthModal"; // 登录/注册弹窗组件
import { ShoppingCart } from "lucide-react";
import { useCartStore } from "../stores/cartStore"; // ✅ 新增


// 用户状态栏组件（页面顶部导航区域）
export default function UserStatusBar() {

  const { openCart } = useCartStore(); // ✅ 获取打开函数

  // 从全局状态（Zustand）中获取用户名、角色，以及修改函数
  const { username, role, setUsername, setRole } = useUserStore();

  // 路由跳转函数
  const navigate = useNavigate();

  // 控制登录/注册弹窗的模式（null 表示不显示）
  const [authMode, setAuthMode] = useState<"login" | "register" | null>(null);

  // 退出登录逻辑：清除本地缓存 + 重置状态 + 回到首页
  const handleLogout = () => {
    localStorage.clear();
    setUsername(null);
    setRole(null);
    navigate("/");
  };

  return (
    <div className="px-20 py-3 flex justify-between items-center shadow-sm">

  {/* logo */} 

  <img
  src="/images/logo.png"
  alt="App Logo"
  onClick={() => navigate("/")}
  className="h-10 cursor-pointer object-contain"
/>


      {/* 右边：根据是否登录显示不同按钮 */}
      <div className="flex items-center space-x-4">

        {/* 登录后显示“订单” + “登出”按钮 */}
        {username ? (
          <>
            <button
              onClick={() =>
                navigate(role === "MERCHANT" ? "/merchant" : "/my-orders")
              }
              className="btn"
            >
              📦 {role === "MERCHANT" ? "Merchant Orders" : "My Orders"}
            </button>

            <button onClick={handleLogout} className="btn">Logout</button>
          </>
        ) : (
          
          // 未登录时显示“登录 / 注册”按钮
          <>
            
            <button
             onClick={() => setAuthMode("login")}
             className="bg-pink-100 
             text-gray-800 
             px-6 py-1.5 
             rounded-full 
             font-medium hover:bg-pink-200 transition text-sm "
            >
              LOGIN
            </button>

            <button
onClick={openCart}
  className="bg-red-700 text-white rounded-full p-1.5 hover:bg-red-800 transition"
>
  <ShoppingCart className="w-5 h-5" />
</button>
          </>
        )}
      </div>



      {/* 登录/注册弹窗，根据 authMode 决定显示与否 */}
      {authMode && (
  <AuthModal
    mode={authMode}
    setMode={setAuthMode} // ✅ 加上这个
    onClose={() => setAuthMode(null)}
    onLoginSuccess={(name) => {
      setUsername(name);
      setAuthMode(null);
    }}
  />
)}

    </div>
  );
}
