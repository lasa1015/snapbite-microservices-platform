// src/layouts/MainLayout.tsx
import { Outlet } from "react-router-dom";
import CartSidebar from "../components/CartSidebar";
import UserStatusBar from "../components/UserStatusBar"; // 👈 新加

export default function MainLayout() {
  return (
    <div style={{ display: "flex", flexDirection: "column", minHeight: "100vh" }}>
      <UserStatusBar /> {/* 👈 放最上面 */}
      <div style={{ display: "flex", flex: 1 }}>
        <div style={{ flex: 1 }}>
          <Outlet />
        </div>
        <CartSidebar />
      </div>
    </div>
  );
}
