import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import App from './App';
import MenuPage from './MenuPage';
import MainLayout from './MainLayout';
import { CartProvider } from './CartContext'; // 👈 新加

ReactDOM.createRoot(document.getElementById('root')!).render(
  <BrowserRouter>
    <CartProvider>
      <Routes>
        <Route path="/" element={<MainLayout />}>
          <Route index element={<App />} />
          <Route path="restaurant/:restaurantId/menu" element={<MenuPage />} />
        </Route>
      </Routes>
    </CartProvider>
  </BrowserRouter>
);
