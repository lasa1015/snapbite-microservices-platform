// src/layouts/MainLayout.tsx
import { Outlet, useLocation } from "react-router-dom";
import CartSidebar from "../components/CartSidebar";
import UserStatusBar from "../components/UserStatusBar";

export default function MainLayout() {
  const location = useLocation();

  // 👇 根据路径判断是否隐藏购物车
  const hideCart = location.pathname.startsWith("/merchant") || location.pathname.startsWith("/checkout");

  return (
    <div style={{ display: "flex", flexDirection: "column", minHeight: "100vh" }}>
      <UserStatusBar />
      <div style={{ display: "flex", flex: 1 }}>
        <div style={{ flex: 1 }}>
          <Outlet />
        </div>
        {!hideCart && <CartSidebar />} {/* ✅ 条件渲染购物车 */}
      </div>
    </div>
  );
}
