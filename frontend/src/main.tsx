import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import App from './App';
import MenuPage from './MenuPage.tsx'; // 我们接下来会创建这个组件

ReactDOM.createRoot(document.getElementById('root')!).render(
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<App />} />
      <Route path="/restaurant/:restaurantId/menu" element={<MenuPage />} />
    </Routes>
  </BrowserRouter>
);
