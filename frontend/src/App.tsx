// src/App.tsx
import { useEffect } from "react";
import { useUserStore } from "./stores/userStore";
import { Routes, Route, Navigate } from "react-router-dom";

import MainLayout from "./layouts/MainLayout";
import HomePage from "./pages/RestaurantsPage";
import MenuPage from "./pages/MenuPage";
import CheckoutPage from "./pages/CheckoutPage";
import MyOrdersPage from "./pages/MyOrdersPage";
import MerchantDashboard from "./pages/MerchantDashboard";

export default function App() {
  const fetchUser = useUserStore((state) => state.fetchUser);

  useEffect(() => {
    fetchUser(); // ✅ 页面初始化时尝试获取用户信息
  }, []);

  return (
    <Routes>
      <Route path="/" element={<MainLayout />}>
        <Route index element={<HomePage />} />
        <Route path="restaurant/:restaurantId/menu" element={<MenuPage />} />
        <Route path="restaurant" element={<Navigate to="/" />} />
        <Route path="checkout" element={<CheckoutPage />} />
        <Route path="my-orders" element={<MyOrdersPage />} />
        <Route path="/merchant" element={<MerchantDashboard />} />
      </Route>
    </Routes>
  );
}
