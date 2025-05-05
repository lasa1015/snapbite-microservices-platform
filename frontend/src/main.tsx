import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { CartProvider } from './context/CartContext';
import { UserProvider } from './context/UserContext';
import MainLayout from './layouts/MainLayout';
import HomePage from './pages/HomePage';
import MenuPage from './pages/MenuPage';
import { FilterProvider } from './context/FilterContext';
import CheckoutPage from "./pages/CheckoutPage"; // ← 引入
import MyOrdersPage from './pages/MyOrdersPage';

ReactDOM.createRoot(document.getElementById('root')!).render(
  <BrowserRouter>
    <UserProvider>
      <CartProvider>
      <FilterProvider>
        <Routes>
          <Route path="/" element={<MainLayout />}>
            <Route index element={<HomePage />} />
            <Route path="restaurant/:restaurantId/menu" element={<MenuPage />} />
            <Route path="restaurant" element={<Navigate to="/" />} />
            <Route path="checkout" element={<CheckoutPage />} />
            <Route path="my-orders" element={<MyOrdersPage />} />
          </Route>
        </Routes>
        </FilterProvider>
      </CartProvider>
    </UserProvider>
  </BrowserRouter>
);
