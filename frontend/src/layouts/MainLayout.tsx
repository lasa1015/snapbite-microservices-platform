// src/layouts/MainLayout.tsx
import { Outlet, useLocation } from "react-router-dom";
import CartSidebar from "../components/CartSidebar";
import UserStatusBar from "../components/UserStatusBar";
import { useCartStore } from "../stores/cartStore"; // ✅ 新增


export default function MainLayout() {
  const location = useLocation();
  const { cartOpen } = useCartStore(); // ✅ 新增

  const hideCart =
    location.pathname.startsWith("/merchant") ||
    location.pathname.startsWith("/checkout") ||
    location.pathname.startsWith("/my-orders");

  return (
    <div className="relative min-h-screen flex flex-col">
      <UserStatusBar />
      <div className="flex flex-1">
        <Outlet />
      </div>

      {/* ✅ 购物车脱离布局，浮动在最右侧 */}
      {!hideCart && cartOpen && <CartSidebar />}
    </div>
  );
}

