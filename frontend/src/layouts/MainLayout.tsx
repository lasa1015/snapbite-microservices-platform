import { Outlet, useLocation } from "react-router-dom";
import CartSidebar from "../components/CartSidebar";
import UserStatusBar from "../components/UserStatusBar";
import { useCartStore } from "../stores/cartStore";

export default function MainLayout() {
  const location = useLocation();
  const { cartOpen, closeCart } = useCartStore();

  const hideCart =
    location.pathname.startsWith("/merchant") ||
    location.pathname.startsWith("/checkout");

  return (
    <div className="relative min-h-screen flex flex-col">
      <UserStatusBar />

      <div className="flex flex-1">
  <div className="w-full">
    <Outlet />
  </div>
</div>


      {/* 遮罩层（放在 CartSidebar 背后） */}
      {!hideCart && cartOpen && (
        <div
          className="fixed inset-0 bg-black bg-opacity-30 z-40"
          onClick={closeCart} // 点击遮罩也关闭购物车（可选）
        ></div>
      )}

      {/* 购物车侧边栏 */}
      {!hideCart && cartOpen && <CartSidebar />}
    </div>
  );
}
