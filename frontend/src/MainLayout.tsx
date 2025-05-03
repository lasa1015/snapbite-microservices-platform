// src/MainLayout.tsx
import { Outlet } from "react-router-dom";
import CartSidebar from "./CartSidebar";

export default function MainLayout() {
  return (
    <div style={{ display: "flex" }}>
      <div style={{ flex: 1 }}>
        <Outlet /> {/* 渲染当前子页面（App or MenuPage） */}
      </div>
      <CartSidebar />
    </div>
  );
}
